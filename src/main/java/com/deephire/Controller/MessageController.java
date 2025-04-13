package com.deephire.Controller;

import jakarta.annotation.PostConstruct;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
public class MessageController {
    @GetMapping("/message")
    public ResponseEntity<List<String>> getMessage() {
        return ResponseEntity.ok(Arrays.asList("Hello", "World"));
    }


    @PostConstruct
    public void init() {
        System.out.println("✅ MessageController is loaded!");
    }


}
