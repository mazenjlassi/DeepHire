package com.deephire.Controller;


import com.deephire.Service.JobPostingService;
import com.deephire.models.JobPosting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/job-posting")
public class JobPostingRestController {

    @Autowired
    private JobPostingService jobPostingService;

    @PostMapping("/add")
    public ResponseEntity<JobPosting> add(@RequestBody JobPosting jobPosting) {
        try {
            JobPosting savedJobPosting = jobPostingService.add(jobPosting);
            return new ResponseEntity<>(savedJobPosting, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<JobPosting>> all() {
        try {
            List<JobPosting> jobPostings = jobPostingService.findAll();
            if (jobPostings.isEmpty()) {
                return new ResponseEntity<>(jobPostings, HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(jobPostings, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/findById/{id}")
    public ResponseEntity<JobPosting> findById(@PathVariable Long id) {
        try {
            JobPosting jobPosting = jobPostingService.find(id);
            if (jobPosting != null) {
                return new ResponseEntity<>(jobPosting, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<JobPosting> update(@PathVariable Long id, @RequestBody JobPosting jobPosting) {
        try {
            JobPosting existingJobPosting = jobPostingService.find(id);
            if (existingJobPosting != null) {
                JobPosting updatedJobPosting = jobPostingService.update(jobPosting);
                return new ResponseEntity<>(updatedJobPosting, HttpStatus.OK);
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
            jobPostingService.delete(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}