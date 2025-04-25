package com.deephire.Controllers;

import com.deephire.Models.User;
import com.deephire.Repositories.UserRepository;
import com.deephire.Service.EmailService;
import com.deephire.Service.RHCompanyService;
import com.deephire.Models.RHCompany;
import com.deephire.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rh-companies")
public class RHCompanyRestController {


    @Autowired
    private RHCompanyService rhCompanyService;
    @Autowired
    private UserService userService;

//    @PostMapping("/add")
//    public ResponseEntity<RHCompany> add(@RequestBody RHCompany rhCompany) {
//        try {
//            RHCompany newRHCompany = rhCompanyService.add(rhCompany);
//            userService.sendEmail(rhCompany);
//            return new ResponseEntity<>(newRHCompany, HttpStatus.CREATED);
//        } catch (Exception e) {
//            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }


    @Autowired
    private JavaMailSender mailSender; // Directly inject JavaMailSender

    @Autowired
    EmailService  emailService;

    @PostMapping("/add")
    public ResponseEntity<RHCompany> add(@RequestBody RHCompany rhCompany) {
        try {
            RHCompany newRHCompany = rhCompanyService.add(rhCompany);


            // Send email directly
           // sendWelcomeEmail(rhCompany.getEmail(), rhCompany.getUsername());

            return new ResponseEntity<>(newRHCompany, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private void sendWelcomeEmail(String email, String name) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("amirouni162@gmail.com");
        message.setTo(email);
        message.setSubject("Welcome to DeepHire!");
        message.setText("Hello " + name + ",\n\nThank you for joining DeepHire!");

        mailSender.send(message);
    }
    @GetMapping("/all")
    public ResponseEntity<List<RHCompany>> all() {
        try {
            List<RHCompany> rhCompanies = rhCompanyService.findAll();
            if (rhCompanies.isEmpty()) {
                return new ResponseEntity<>(rhCompanies, HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(rhCompanies, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // New endpoint to get all recruiters with ROLE_RECRUITER
    @GetMapping("/recruiters")
    public ResponseEntity<List<RHCompany>> getAllRecruiters() {
        try {
            List<RHCompany> recruiters = rhCompanyService.findAllRecruiters();
            if (recruiters.isEmpty()) {
                return new ResponseEntity<>(recruiters, HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(recruiters, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/findById/{id}")
    public ResponseEntity<RHCompany> findById(@PathVariable Long id) {
        try {
            RHCompany rhCompany = rhCompanyService.find(id);
            return new ResponseEntity<>(rhCompany, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<RHCompany> update(@PathVariable Long id, @RequestBody RHCompany rhCompany) {
        try {
            RHCompany existingRHCompany = rhCompanyService.find(id);
            if (existingRHCompany != null) {
                rhCompany.setId(id);
                RHCompany updatedRHCompany = rhCompanyService.update(rhCompany);
                return new ResponseEntity<>(updatedRHCompany, HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable Long id) {
        try {
            rhCompanyService.delete(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}