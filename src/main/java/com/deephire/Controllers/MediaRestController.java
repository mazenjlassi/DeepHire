package com.deephire.Controllers;

import com.deephire.Service.MediaService;
import com.deephire.Models.Media;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/media")
public class MediaRestController {

    @Autowired
    private MediaService mediaService;

    @PostMapping("/add")
    public ResponseEntity<Media> add(@RequestBody Media media) {
        try {
            mediaService.add(media);
            return new ResponseEntity<>(media, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<Media>> all() {
        try {
            List<Media> mediaList = mediaService.findAll();
            if (mediaList.isEmpty()) {
                return new ResponseEntity<>(mediaList, HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(mediaList, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/findById/{id}")
    public ResponseEntity<Media> findById(@PathVariable Long id) {
        Media media = mediaService.find(id);
        if (media != null) {
            return new ResponseEntity<>(media, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Media> update(@PathVariable Long id, @RequestBody Media media) {
        Media existingMedia = mediaService.find(id);
        if (existingMedia != null) {
            mediaService.update(media);
            return new ResponseEntity<>(media, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable Long id) {
        try {
            mediaService.delete(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}