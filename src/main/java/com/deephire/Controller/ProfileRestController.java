package com.deephire.Controller;


import com.deephire.Service.ProfileService;
import com.deephire.models.Profile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/profiles")
public class ProfileRestController {

    @Autowired
    private ProfileService profileService;

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
}
