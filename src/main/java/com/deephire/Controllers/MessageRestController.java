package com.deephire.Controllers;


import com.deephire.Service.MessageService;
import com.deephire.Models.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/messages")
public class MessageRestController {

    @Autowired
    private MessageService messageService;

    @PostMapping("/add")
    public ResponseEntity<Message> add(@RequestBody Message message) {
        try {
            Message newMessage = messageService.add(message);
            return new ResponseEntity<>(newMessage, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<Message>> all() {
        try {
            List<Message> messages = messageService.findAll();
            if (messages.isEmpty()) {
                return new ResponseEntity<>(messages, HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(messages, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/findById/{id}")
    public ResponseEntity<Message> findById(@PathVariable Long id) {
        try {
            Message message = messageService.find(id);
            return new ResponseEntity<>(message, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Message> update(@PathVariable Long id, @RequestBody Message message) {
        try {
            Message existingMessage = messageService.find(id);
            if (existingMessage != null) {
                Message updatedMessage = messageService.update(message);
                return new ResponseEntity<>(updatedMessage, HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable Long id) {
        try {
            messageService.delete(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
