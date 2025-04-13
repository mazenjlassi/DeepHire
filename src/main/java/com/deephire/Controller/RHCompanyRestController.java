package com.deephire.Controller;

import com.deephire.Service.RHCompanyService;
import com.deephire.models.RHCompany;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rh-companies")
public class RHCompanyRestController {

    @Autowired
    private RHCompanyService rhCompanyService;

    @PostMapping("/add")
    public ResponseEntity<RHCompany> add(@RequestBody RHCompany rhCompany) {
        try {
            RHCompany newRHCompany = rhCompanyService.add(rhCompany);
            return new ResponseEntity<>(newRHCompany, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
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
