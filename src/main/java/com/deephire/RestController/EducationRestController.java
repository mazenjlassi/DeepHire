package com.deephire.RestController;


import com.deephire.Service.EducationService;
import com.deephire.models.Education;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/education")
public class EducationRestController {

    @Autowired
    private EducationService educationService;

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
}