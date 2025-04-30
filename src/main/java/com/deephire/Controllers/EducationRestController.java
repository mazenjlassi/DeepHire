package com.deephire.Controllers;


import com.deephire.Dto.EducationDto;
import com.deephire.JWT.JwtUtils;
import com.deephire.Models.Profile;
import com.deephire.Models.User;
import com.deephire.Repositories.ProfileRepository;
import com.deephire.Repositories.UserRepository;
import com.deephire.Service.EducationService;
import com.deephire.Models.Education;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/education")
public class EducationRestController {

    @Autowired
    private EducationService educationService;



    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtils jwtUtils;


    @Autowired
    private ProfileRepository profileRepository;

    @PostMapping("/add")
    public ResponseEntity<Education> add(@RequestBody Education education) {
        try {
            Education savedEducation = educationService.add(education);
            return new ResponseEntity<>(savedEducation, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<Education>> all() {
        try {
            List<Education> educations = educationService.findAll();
            if (educations.isEmpty()) {
                return new ResponseEntity<>(educations, HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(educations, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/findById/{id}")
    public ResponseEntity<Education> findById(@PathVariable Long id) {
        try {
            Education education = educationService.find(id);
            if (education != null) {
                return new ResponseEntity<>(education, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Education> update(@PathVariable Long id, @RequestBody Education education) {
        try {
            Education existingEducation = educationService.find(id);
            if (existingEducation != null) {
                Education updatedEducation = educationService.update(education);
                return new ResponseEntity<>(updatedEducation, HttpStatus.OK);
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
            educationService.delete(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/add-education")
    public ResponseEntity<?> addEducation(@RequestBody EducationDto educationDto,
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

            if (profile.getEducation() == null) {
                profile.setEducation(new ArrayList<>());
            }

            // 🔍 Check if education already exists (by schoolName, degree, and startDate)
            boolean isDuplicate = profile.getEducation().stream().anyMatch(edu ->
                    edu.getSchoolName().equalsIgnoreCase(educationDto.getSchoolName()) &&
                            edu.getDegree().equalsIgnoreCase(educationDto.getDegree()) &&
                            edu.getStartDate().equals(educationDto.getStartDate())
            );

            if (isDuplicate) {
                return new ResponseEntity<>("Education entry already exists.", HttpStatus.CONFLICT);
            }

            Education education = new Education();
            education.setDegree(educationDto.getDegree());
            education.setSchoolName(educationDto.getSchoolName());
            education.setFieldOfStudy(educationDto.getFieldOfStudy());
            education.setStartDate(educationDto.getStartDate());
            education.setEndDate(educationDto.getEndDate());
            education.setProfile(profile);

            profile.getEducation().add(education);
            profileRepository.save(profile); // or educationRepository.save(education);

            return new ResponseEntity<>(education, HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    @PostMapping("/delete-education")
    public ResponseEntity<?> deleteEducation(@RequestHeader("Authorization") String token,
                                             @RequestBody EducationDto educationDto) {
        try {
            String username = jwtUtils.getUserNameFromJwtToken(token.substring(7));
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            Profile profile = user.getProfile();
            if (profile == null || profile.getEducation() == null) {
                return new ResponseEntity<>("No education entries found", HttpStatus.NOT_FOUND);
            }

            Education educationToRemove = profile.getEducation().stream()
                    .filter(e -> e.getDegree().equalsIgnoreCase(educationDto.getDegree()) &&
                            e.getSchoolName().equalsIgnoreCase(educationDto.getSchoolName()) &&
                            Objects.equals(e.getFieldOfStudy(), educationDto.getFieldOfStudy()))
                    .findFirst()
                    .orElse(null);

            if (educationToRemove == null) {
                return new ResponseEntity<>("Education not found", HttpStatus.NOT_FOUND);
            }

            profile.getEducation().remove(educationToRemove);
            educationService.delete(educationToRemove);

            return new ResponseEntity<>("Education deleted successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error deleting education", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



}