package com.deephire.Controllers;


import com.deephire.Dto.ExperienceDto;
import com.deephire.Dto.SkillDto;
import com.deephire.JWT.JwtUtils;
import com.deephire.Models.Profile;
import com.deephire.Models.Skill;
import com.deephire.Models.User;
import com.deephire.Repositories.ProfileRepository;
import com.deephire.Repositories.UserRepository;
import com.deephire.Service.ExperienceService;
import com.deephire.Models.Experience;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/experience")
public class ExperienceRestController {

    @Autowired
    private ExperienceService experienceService;


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtils jwtUtils;


    @Autowired
    private ProfileRepository profileRepository;

    @PostMapping("/add")
    public ResponseEntity<Experience> add(@RequestBody Experience experience) {
        try {
            Experience savedExperience = experienceService.add(experience);
            return new ResponseEntity<>(savedExperience, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<Experience>> all() {
        try {
            List<Experience> experiences = experienceService.findAll();
            if (experiences.isEmpty()) {
                return new ResponseEntity<>(experiences, HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(experiences, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/findById/{id}")
    public ResponseEntity<Experience> findById(@PathVariable Long id) {
        try {
            Experience experience = experienceService.find(id);
            if (experience != null) {
                return new ResponseEntity<>(experience, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Experience> update(@PathVariable Long id, @RequestBody Experience experience) {
        try {
            Experience existingExperience = experienceService.find(id);
            if (existingExperience != null) {
                Experience updatedExperience = experienceService.update(experience);
                return new ResponseEntity<>(updatedExperience, HttpStatus.OK);
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
            experienceService.delete(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PostMapping(value = "/add-experience", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> completeProfile(
            @RequestHeader("Authorization") String token,
            @RequestBody ExperienceDto experienceDto) {

        try {
            String username = jwtUtils.getUserNameFromJwtToken(token.substring(7));
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            Profile profile = user.getProfile();
            if (profile == null) {
                profile = new Profile();
                user.setProfile(profile);
                profile.setUser(user);
            }

            if (profile.getExperiences() == null) {
                profile.setExperiences(new ArrayList<>());
            }

            // Check for duplicate experience (based on companyName, title, startDate)
            boolean isDuplicate = profile.getExperiences().stream().anyMatch(exp ->
                    exp.getCompanyName().equalsIgnoreCase(experienceDto.getCompanyName()) &&
                            exp.getTitle().equalsIgnoreCase(experienceDto.getTitle()) &&
                            exp.getStartDate().equals(experienceDto.getStartDate())
            );

            if (isDuplicate) {
                return new ResponseEntity<>("Experience already exists.", HttpStatus.CONFLICT);
            }

            Experience experience = new Experience();
            experience.setCompanyName(experienceDto.getCompanyName());
            experience.setTitle(experienceDto.getTitle());
            experience.setStartDate(experienceDto.getStartDate());
            experience.setEndDate(experienceDto.getEndDate());
            experience.setDescription(experienceDto.getDescription());
            experience.setProfile(profile);

            profile.getExperiences().add(experience);
            profileRepository.save(profile);

            return new ResponseEntity<>(experience, HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PostMapping("/delete-experience")
    public ResponseEntity<?> deleteExperience(@RequestHeader("Authorization") String token,
                                              @RequestBody ExperienceDto experienceDto) {
        try {
            String username = jwtUtils.getUserNameFromJwtToken(token.substring(7));
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            Profile profile = user.getProfile();
            if (profile == null || profile.getExperiences() == null) {
                return new ResponseEntity<>("No experiences found", HttpStatus.NOT_FOUND);
            }

            Experience experienceToRemove = profile.getExperiences().stream()
                    .filter(e -> e.getCompanyName().equalsIgnoreCase(experienceDto.getCompanyName()) &&
                            e.getTitle().equalsIgnoreCase(experienceDto.getTitle()))
                    .findFirst()
                    .orElse(null);

            if (experienceToRemove == null) {
                return new ResponseEntity<>("Experience not found", HttpStatus.NOT_FOUND);
            }

            profile.getExperiences().remove(experienceToRemove);
            experienceService.delete(experienceToRemove);

            return new ResponseEntity<>("Experience deleted successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error deleting experience", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
