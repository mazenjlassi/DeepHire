package com.deephire.Controllers;


import com.deephire.Dto.SkillDto;
import com.deephire.JWT.JwtUtils;
import com.deephire.Models.Profile;
import com.deephire.Models.User;
import com.deephire.Repositories.ProfileRepository;
import com.deephire.Repositories.UserRepository;
import com.deephire.Service.SkillService;
import com.deephire.Models.Skill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/skills")
public class SkillRestController {

    @Autowired
    private SkillService skillService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtils jwtUtils;


    @Autowired
    private ProfileRepository profileRepository;

    @PostMapping("/add")
    public ResponseEntity<Skill> add(@RequestBody Skill skill) {
        try {
            Skill newSkill = skillService.add(skill);
            return new ResponseEntity<>(newSkill, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<Skill>> all() {
        try {
            List<Skill> skills = skillService.findAll();
            if (skills.isEmpty()) {
                return new ResponseEntity<>(skills, HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(skills, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/findById/{id}")
    public ResponseEntity<Skill> findById(@PathVariable Long id) {
        try {
            Skill skill = skillService.find(id);
            return new ResponseEntity<>(skill, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Skill> update(@PathVariable Long id, @RequestBody Skill skill) {
        try {
            Skill existingSkill = skillService.find(id);
            if (existingSkill != null) {
                Skill updatedSkill = skillService.update(skill);
                return new ResponseEntity<>(updatedSkill, HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable Long id) {
        try {
            skillService.delete(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PostMapping(value = "/add-skill", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addSkill(
            @RequestHeader("Authorization") String token,
            @RequestBody SkillDto skillDto) {

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

            if (profile.getSkills() == null) {
                profile.setSkills(new ArrayList<>());
            }

            // 🔍 Check for duplicate skill name (case-insensitive)
            boolean isDuplicate = profile.getSkills().stream()
                    .anyMatch(existingSkill ->
                            existingSkill.getName().equalsIgnoreCase(skillDto.getName())
                    );

            if (isDuplicate) {
                return new ResponseEntity<>("Skill already exists in the profile.", HttpStatus.CONFLICT);
            }

            Skill skill = new Skill();
            skill.setName(skillDto.getName());
            skill.setProfile(profile);
            profile.getSkills().add(skill);

            profileRepository.save(profile);

            return new ResponseEntity<>(skill, HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PostMapping("/delete-skill")
    public ResponseEntity<Boolean> deleteSkill(@RequestHeader("Authorization") String token,
                                               @RequestBody SkillDto skillDto) {
        try {
            String username = jwtUtils.getUserNameFromJwtToken(token.substring(7));
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            Profile profile = user.getProfile();
            if (profile == null || profile.getSkills() == null) {
                return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
            }

            Skill skillToRemove = profile.getSkills().stream()
                    .filter(s -> s.getName().equalsIgnoreCase(skillDto.getName()))
                    .findFirst()
                    .orElse(null);

            if (skillToRemove == null) {
                return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
            }

            profile.getSkills().remove(skillToRemove);
            skillService.delete(skillToRemove);

            return ResponseEntity.ok(true);
        } catch (Exception e) {
            return new ResponseEntity<>(false, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
