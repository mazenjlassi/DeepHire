package com.deephire.Controllers;


import com.deephire.Service.SkillService;
import com.deephire.Models.Skill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/skills")
public class SkillRestController {

    @Autowired
    private SkillService skillService;

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
}
