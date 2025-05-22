package com.deephire.Controllers;


import com.deephire.Dto.JobApplicationDTO;
import com.deephire.Enums.Status;
import com.deephire.Repositories.StatusOfCvRepository;
import com.deephire.Service.StatusOfCvService;
import com.deephire.Models.StatusOfCv;
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
    @Autowired
    private StatusOfCvRepository statusOfCvRepository;

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

    @PutMapping("/approve")
    public ResponseEntity<?> approve(@RequestBody JobApplicationDTO jobApplicationDTO) {
        try {
            // Fetch the StatusOfCv using userId and jobPostingId
            StatusOfCv statusOfCv = statusOfCvRepository.findByUserIdAndJobPostingId(
                    jobApplicationDTO.getUser().getId(),
                    (long) jobApplicationDTO.getId()
            ).orElse(null);

            if (statusOfCv == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Application not found");
            }

            statusOfCv.setState(Status.APPROVED);
            statusOfCvService.update(statusOfCv);

            return ResponseEntity.status(HttpStatus.OK).body(true);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error while approving: " + e.getMessage());
        }
    }


    @PutMapping("/reject")
    public ResponseEntity<?> reject(@RequestBody JobApplicationDTO jobApplicationDTO) {
        try {
            // Fetch the StatusOfCv using userId and jobPostingId
            StatusOfCv statusOfCv = statusOfCvRepository.findByUserIdAndJobPostingId(
                    jobApplicationDTO.getUser().getId(),
                    (long) jobApplicationDTO.getId()
            ).orElse(null);

            if (statusOfCv == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Application not found");
            }

            statusOfCv.setState(Status.REJECTED);
            statusOfCvService.update(statusOfCv);

            return ResponseEntity.status(HttpStatus.OK).body(true);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error while rejecting: " + e.getMessage());
        }
    }

}
