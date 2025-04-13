package com.deephire.Controller;


import com.deephire.Service.ExperienceService;
import com.deephire.models.Experience;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/experience")
public class ExperienceRestController {

    @Autowired
    private ExperienceService experienceService;

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
}