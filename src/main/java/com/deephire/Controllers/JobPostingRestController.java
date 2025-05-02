package com.deephire.Controllers;


import com.deephire.Dto.JobCompanyDTO;
import com.deephire.Dto.JobPostingRequestDTO;
import com.deephire.JWT.JwtUtils;
import com.deephire.Models.*;
import com.deephire.Repositories.AdminCompanyRepository;
import com.deephire.Repositories.CompanyRepository;
import com.deephire.Repositories.JobPostingRepository;
import com.deephire.Repositories.UserRepository;
import com.deephire.Service.JobPostingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.batch.BatchProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/job-posting")
public class JobPostingRestController {

    @Autowired
    private JobPostingService jobPostingService;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private AdminCompanyRepository adminCompanyRepository;


    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JobPostingRepository jobPostingRepository;


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
    public ResponseEntity<?> all() {
        try {
            List<JobPosting> jobPostings = jobPostingService.findAll();
            if (jobPostings.isEmpty()) {
                return new ResponseEntity<>(jobPostings, HttpStatus.NO_CONTENT);
            }
            List<JobCompanyDTO> responseDTOs = jobPostings.stream().map(job -> {
                JobCompanyDTO dto = new JobCompanyDTO();
                dto.setTitle(job.getTitle());
                dto.setDescription(job.getDescription());
                dto.setRequirements(job.getRequirements());
                dto.setLocation(job.getLocation());
                dto.setDatePosted(job.getDatePosted());
                dto.setLogo(Base64.getEncoder().encodeToString(job.getCompany().getLogo()));
                dto.setCompany(job.getCompany().getName());
                return dto;
            }).toList();


            return new ResponseEntity<>(responseDTOs, HttpStatus.OK);
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



    @PostMapping("/add-job-posting")
    public ResponseEntity<?> addJobPosting(@RequestBody JobPostingRequestDTO jobPostingRequestDTO,
                                           @RequestHeader("Authorization") String token) {
        try {
            if (token == null || !token.startsWith("Bearer ")) {
                return new ResponseEntity<>("Token is missing or invalid", HttpStatus.BAD_REQUEST);
            }

            String username = jwtUtils.getUserNameFromJwtToken(token.substring(7));
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            Company company;

            if (user instanceof AdminCompany adminCompany) {
                company = companyRepository.getCompanyByAdmin(adminCompany);
            } else if (user instanceof RHCompany rhCompany) {
                company = rhCompany.getCompany(); // relation ManyToOne
            } else {
                return new ResponseEntity<>("User must be AdminCompany or RHCompany", HttpStatus.UNAUTHORIZED);
            }

            if (company == null) {
                return new ResponseEntity<>("Associated company not found", HttpStatus.NOT_FOUND);
            }

            JobPosting jobPosting = new JobPosting();
            jobPosting.setDatePosted(new Date());
            jobPosting.setCompany(company);
            jobPosting.setDescription(jobPostingRequestDTO.getDescription());
            jobPosting.setLocation(jobPostingRequestDTO.getLocation());
            jobPosting.setTitle(jobPostingRequestDTO.getTitle());
            jobPosting.setRequirements(jobPostingRequestDTO.getRequirements());

            JobPosting saved = jobPostingService.add(jobPosting);

            return new ResponseEntity<>(saved, HttpStatus.CREATED);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }




    @GetMapping("/get-job-posting")
    public ResponseEntity<?> getJobPosting(@RequestHeader("Authorization") String token) {
        try {
            if (token == null || !token.startsWith("Bearer ")) {
                return new ResponseEntity<>("Token is missing or invalid", HttpStatus.BAD_REQUEST);
            }

            String username = jwtUtils.getUserNameFromJwtToken(token.substring(7));
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            Company company;

            if (user instanceof AdminCompany adminCompany) {
                company = companyRepository.getCompanyByAdmin(adminCompany);
            } else if (user instanceof RHCompany rhCompany) {
                company = rhCompany.getCompany(); // relation ManyToOne
            } else {
                return new ResponseEntity<>("User must be AdminCompany or RHCompany", HttpStatus.UNAUTHORIZED);
            }

            if (company == null) {
                return new ResponseEntity<>("Associated company not found", HttpStatus.NOT_FOUND);
            }

            List<JobPosting> jobPostings = jobPostingRepository.findJobPostingByCompany(company);


            List<JobPostingRequestDTO> responseDTOs = jobPostings.stream().map(job -> {
                JobPostingRequestDTO dto = new JobPostingRequestDTO();
                dto.setTitle(job.getTitle());
                dto.setDescription(job.getDescription());
                dto.setRequirements(job.getRequirements());
                dto.setLocation(job.getLocation());
                dto.setDatePosted(job.getDatePosted());
                return dto;
            }).toList();

            return ResponseEntity.ok(responseDTOs);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while retrieving job postings: " + e.getMessage());
        }
    }
}