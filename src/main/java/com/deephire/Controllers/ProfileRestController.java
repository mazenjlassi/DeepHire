package com.deephire.Controllers;


import com.deephire.JWT.JwtUtils;
import com.deephire.Models.User;
import com.deephire.Repositories.UserRepository;
import com.deephire.Request.ProfileCompletionRequest;
import com.deephire.Service.*;
import com.deephire.Models.Profile;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/profiles")
public class ProfileRestController {

    @Autowired
    private ProfileService profileService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private SkillService skillService;

    @Autowired
    private ExperienceService experienceService;

    @Autowired
    private EducationService educationService;

    @Autowired
    private CertificationService certificationService;

    @PostMapping("/add")
    public ResponseEntity<Profile> add(@RequestBody Profile profile) {
        try {
            Profile newProfile = profileService.add(profile);
            return new ResponseEntity<>(newProfile, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<Profile>> all() {
        try {
            List<Profile> profiles = profileService.findAll();
            if (profiles.isEmpty()) {
                return new ResponseEntity<>(profiles, HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(profiles, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/findById/{id}")
    public ResponseEntity<Profile> findById(@PathVariable Long id) {
        try {
            Profile profile = profileService.find(id);
            return new ResponseEntity<>(profile, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Profile> update(@PathVariable Long id, @RequestBody Profile profile) {
        try {
            Profile existingProfile = profileService.find(id);
            if (existingProfile != null) {
                Profile updatedProfile = profileService.update(profile);
                return new ResponseEntity<>(updatedProfile, HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable Long id) {
        try {
            profileService.delete(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PostMapping(value = "/complete-profile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> completeProfile(
            @RequestHeader("Authorization") String token,
            @RequestPart("profileData") String profileDataJson,
            @RequestPart(value = "profilePicture", required = false) MultipartFile profilePicture,
            @RequestPart(value = "backGroundImage", required = false) MultipartFile backGroundImage) {

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            ProfileCompletionRequest request = objectMapper.readValue(profileDataJson, ProfileCompletionRequest.class);

            String username = jwtUtils.getUserNameFromJwtToken(token.substring(7));
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            user.setBio(request.getBio());
            user.setLocation(request.getLocation());
            user.setFirstLogin(true);

            // Handle profile picture
            if (profilePicture != null && !profilePicture.isEmpty()) {
                user.setProfilePicture(profilePicture.getBytes());
            }

            // Handle background image
            if (backGroundImage != null && !backGroundImage.isEmpty()) {
                user.setBackGroundImage(backGroundImage.getBytes());
            }

            Profile profile = user.getProfile();
            if (profile == null) {
                profile = new Profile();
                profile.setUser(user);
            }

            profile.setHeadline(request.getHeadline());
            profile.setSummary(request.getSummary());

            // Sauvegarder d'abord le profil
            Profile savedProfile = profileService.add(profile);

            // Ensuite, sauvegarder les listes liées
            if (request.getSkills() != null)
                skillService.saveAllSkillsWithProfile(request.getSkills(), savedProfile);
            if (request.getExperiences() != null)
                experienceService.saveAllExperiencesWithProfile(request.getExperiences(), savedProfile);
            if (request.getEducation() != null)
                educationService.saveAllEducationWithProfile(request.getEducation(), savedProfile);
            if (request.getCertifications() != null)
                certificationService.saveAllCertificationsWithProfile(request.getCertifications(), savedProfile);

            user.setProfile(savedProfile);

            user.setFirstLogin(false);
            userRepository.save(user);


            return ResponseEntity.ok(new MessageResponse("Profile completed successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new MessageResponse("Error completing profile: " + e.getMessage()));
        }
    }


    @PostMapping("/skip-profile")
    public ResponseEntity<?> skipProfileCompletion(
            @RequestHeader("Authorization") String token) {
        String username = jwtUtils.getUserNameFromJwtToken(token.substring(7));
        User user = userRepository.findByUsername(username).orElseThrow(()->new RuntimeException("User not found"));

        user.setFirstLogin(false);
        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("Profile completion skipped"));
    }
}
