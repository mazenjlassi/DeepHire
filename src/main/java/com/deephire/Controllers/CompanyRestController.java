package com.deephire.Controllers;


import com.deephire.Repositories.CompanyRepository;
import com.deephire.Service.CompanyService;
import com.deephire.Models.Company;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/company")
public class CompanyRestController {

    @Autowired
    private CompanyService companyService;
    @Autowired
    private CompanyRepository companyRepository;

    @PostMapping("/add")
    public ResponseEntity<Company> add(@RequestBody Company company) {
        try {
            Company savedCompany = companyService.add(company);
            return new ResponseEntity<>(savedCompany, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<Company>> all() {
        try {
            List<Company> companies = companyService.findAll();
            if (companies.isEmpty()) {
                return new ResponseEntity<>(companies, HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(companies, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/findById/{id}")
    public ResponseEntity<Company> findById(@PathVariable Long id) {
        try {
            Company company = companyService.find(id);
            if (company != null) {
                return new ResponseEntity<>(company, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Company> update(@PathVariable Long id, @RequestBody Company company) {
        try {
            Company existingCompany = companyService.find(id);
            if (existingCompany != null) {
                Company updatedCompany = companyService.update(company);
                return new ResponseEntity<>(updatedCompany, HttpStatus.OK);
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
            companyService.delete(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }




}