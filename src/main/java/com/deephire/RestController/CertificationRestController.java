package com.deephire.RestController;

import com.deephire.Service.CertificationService;
import com.deephire.models.Certification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/certification")
public class CertificationRestController {

    @Autowired
    private CertificationService certificationService;

    @PostMapping("/add")
    public ResponseEntity<Certification> add(@RequestBody Certification certification) {
        try {
            Certification savedCertification = certificationService.add(certification);
            return new ResponseEntity<>(savedCertification, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<Certification>> all() {
        try {
            List<Certification> certifications = certificationService.findAll();
            if (certifications.isEmpty()) {
                return new ResponseEntity<>(certifications, HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(certifications, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/findById/{id}")
    public ResponseEntity<Certification> findById(@PathVariable Long id) {
        try {
            Certification certification = certificationService.find(id);
            if (certification != null) {
                return new ResponseEntity<>(certification, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Certification> update(@PathVariable Long id, @RequestBody Certification certification) {
        try {
            Certification existingCertification = certificationService.find(id);
            if (existingCertification != null) {
                Certification updatedCertification = certificationService.update(certification);
                return new ResponseEntity<>(updatedCertification, HttpStatus.OK);
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
            certificationService.delete(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}