package com.deephire.Controllers;

import com.deephire.Dto.CertificationDto;
import com.deephire.JWT.JwtUtils;
import com.deephire.Models.Profile;
import com.deephire.Models.User;
import com.deephire.Repositories.ProfileRepository;
import com.deephire.Repositories.UserRepository;
import com.deephire.Service.CertificationService;
import com.deephire.Models.Certification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/certification")
public class CertificationRestController {

    @Autowired
    private CertificationService certificationService;



    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtils jwtUtils;


    @Autowired
    private ProfileRepository profileRepository;

    @PostMapping("/add")
    public ResponseEntity<Certification> add(@RequestBody Certification certification) {
        try {
            Certification savedCertification = certificationService.add(certification);
            return new ResponseEntity<>(savedCertification, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<Certification>> all() {
        try {
            List<Certification> certifications = certificationService.findAll();
            if (certifications.isEmpty()) {
                return new ResponseEntity<>(certifications, HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(certifications, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/findById/{id}")
    public ResponseEntity<Certification> findById(@PathVariable Long id) {
        try {
            Certification certification = certificationService.find(id);
            if (certification != null) {
                return new ResponseEntity<>(certification, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Certification> update(@PathVariable Long id, @RequestBody Certification certification) {
        try {
            Certification existingCertification = certificationService.find(id);
            if (existingCertification != null) {
                Certification updatedCertification = certificationService.update(certification);
                return new ResponseEntity<>(updatedCertification, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable Long id) {
        try {
            certificationService.delete(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/add-certification")
    public ResponseEntity<?> addCertification(@RequestBody CertificationDto certificationDto,
                                              @RequestHeader("Authorization") String token) {
        try {
            String username = jwtUtils.getUserNameFromJwtToken(token.substring(7));
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            Profile profile = user.getProfile();
            if (profile == null) {
                profile = new Profile();
                profile.setUser(user);
                user.setProfile(profile);
            }

            if (profile.getCertifications() == null) {
                profile.setCertifications(new ArrayList<>());
            }

            // 🔍 Check if the certification already exists (by name, issuer, issueDate)
            boolean isDuplicate = profile.getCertifications().stream().anyMatch(cert ->
                    cert.getName().equalsIgnoreCase(certificationDto.getName()) &&
                            cert.getIssuer().equalsIgnoreCase(certificationDto.getIssuer()) &&
                            cert.getIssueDate().equals(certificationDto.getIssueDate())
            );

            if (isDuplicate) {
                return new ResponseEntity<>("Certification already exists.", HttpStatus.CONFLICT);
            }

            Certification certification = new Certification();
            certification.setName(certificationDto.getName());
            certification.setIssuer(certificationDto.getIssuer());
            certification.setIssueDate(certificationDto.getIssueDate());
            certification.setExpirationDate(certificationDto.getExpirationDate());
            certification.setProfile(profile);

            profile.getCertifications().add(certification);
            profileRepository.save(profile);

            return new ResponseEntity<>(certification, HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PostMapping("/delete-certification")
    public ResponseEntity<?> deleteCertification(@RequestHeader("Authorization") String token,
                                                 @RequestBody CertificationDto certificationDto) {
        try {
            String username = jwtUtils.getUserNameFromJwtToken(token.substring(7));
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            Profile profile = user.getProfile();
            if (profile == null || profile.getCertifications() == null) {
                return new ResponseEntity<>("No certifications found", HttpStatus.NOT_FOUND);
            }

            Certification certToRemove = profile.getCertifications().stream()
                    .filter(c -> c.getName().equalsIgnoreCase(certificationDto.getName()) &&
                            c.getIssuer().equalsIgnoreCase(certificationDto.getIssuer()))
                    .findFirst()
                    .orElse(null);

            if (certToRemove == null) {
                return new ResponseEntity<>("Certification not found", HttpStatus.NOT_FOUND);
            }

            profile.getCertifications().remove(certToRemove);
            certificationService.delete(certToRemove);

            return new ResponseEntity<>("Certification deleted successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error deleting certification", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}