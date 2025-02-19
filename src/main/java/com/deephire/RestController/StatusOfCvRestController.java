package com.deephire.RestController;


import com.deephire.Service.StatusOfCvService;
import com.deephire.models.StatusOfCv;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/status-of-cv")
public class StatusOfCvRestController {

    @Autowired
    private StatusOfCvService statusOfCvService;

    @PostMapping("/add")
    public ResponseEntity<StatusOfCv> add(@RequestBody StatusOfCv statusOfCv) {
        try {
            statusOfCvService.add(statusOfCv);
            return new ResponseEntity<>(statusOfCv, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<StatusOfCv>> all() {
        try {
            List<StatusOfCv> statusList = statusOfCvService.findAll();
            if (statusList.isEmpty()) {
                return new ResponseEntity<>(statusList, HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(statusList, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/findById/{id}")
    public ResponseEntity<StatusOfCv> findById(@PathVariable Long id) {
        StatusOfCv statusOfCv = statusOfCvService.find(id);
        if (statusOfCv != null) {
            return new ResponseEntity<>(statusOfCv, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<StatusOfCv> update(@PathVariable Long id, @RequestBody StatusOfCv statusOfCv) {
        StatusOfCv existingStatus = statusOfCvService.find(id);
        if (existingStatus != null) {
            statusOfCvService.update(statusOfCv);
            return new ResponseEntity<>(statusOfCv, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable Long id) {
        try {
            statusOfCvService.delete(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
