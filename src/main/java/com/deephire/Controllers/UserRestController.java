package com.deephire.Controllers;


import com.deephire.Dto.UserSearchDTO;
import com.deephire.JWT.JwtUtils;
import com.deephire.Models.Skill;
import com.deephire.Repositories.UserRepository;
import com.deephire.Service.MessageResponse;
import com.deephire.Service.UserService;
import com.deephire.Models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserRestController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping("/add")
    public ResponseEntity<User> add(@RequestBody User user) {
        try {
            userService.add(user);
            return new ResponseEntity<>(user, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<User>> all() {
        try {
            List<User> users = userService.findAll();
            if (users.isEmpty()) {
                return new ResponseEntity<>(users, HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(users, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    

    @PutMapping(value = "/update/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateUser(
            @PathVariable Long id,
            @RequestParam("username") String username,
            @RequestParam("email") String email,
            @RequestParam("firstName") String firstName,
            @RequestParam("lastName") String lastName,
            @RequestParam(value = "bio", required = false) String bio,
            @RequestParam(value = "location", required = false) String location,
            @RequestParam(value = "profilePicture", required = false) MultipartFile profilePicture,
            @RequestParam(value = "backGroundImage", required = false) MultipartFile backGroundImage
    ) {
        try {
            User existingUser = userService.find(id);

            existingUser.setUsername(username);
            existingUser.setEmail(email);
            existingUser.setFirstName(firstName);
            existingUser.setLastName(lastName);
            existingUser.setBio(bio);
            existingUser.setLocation(location);

            if (profilePicture != null && !profilePicture.isEmpty()) {
                existingUser.setProfilePicture(profilePicture.getBytes());
            }

            if (backGroundImage != null && !backGroundImage.isEmpty()) {
                existingUser.setBackGroundImage(backGroundImage.getBytes());
            }

            userService.update(existingUser); // You need an update method in your service/repo
            return ResponseEntity.ok(existingUser);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Update failed: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    @PutMapping(value = "/update-my-profile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateCurrentUser(
            @RequestHeader("Authorization") String token,
            @RequestParam("email") String email,
            @RequestParam("firstName") String firstName,
            @RequestParam("lastName") String lastName,
            @RequestParam(value = "bio", required = false) String bio,
            @RequestParam(value = "location", required = false) String location,
            @RequestParam(value = "profilePicture", required = false) MultipartFile profilePicture,
            @RequestParam(value = "backGroundImage", required = false) MultipartFile backGroundImage
    ) {

        try {

            String userNameFromJwtToken = jwtUtils.getUserNameFromJwtToken(token.substring(7));
            User existingUser = userRepository.findByUsername(userNameFromJwtToken).orElseThrow(()->new RuntimeException("User not found"));

            existingUser.setUsername(userNameFromJwtToken);
            existingUser.setEmail(email);
            existingUser.setFirstName(firstName);
            existingUser.setLastName(lastName);
            existingUser.setBio(bio);
            existingUser.setLocation(location);

            if (profilePicture != null && !profilePicture.isEmpty()) {
                existingUser.setProfilePicture(profilePicture.getBytes());
            }

            if (backGroundImage != null && !backGroundImage.isEmpty()) {
                existingUser.setBackGroundImage(backGroundImage.getBytes());
            }

            userService.update(existingUser); // You need an update method in your service/repo
            return ResponseEntity.ok(existingUser);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Update failed: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    @DeleteMapping("/delete/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable Long id) {
        try {
            userService.delete(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }






    @GetMapping("/me")
    public ResponseEntity<User> getCurrentUser(
            @RequestHeader("Authorization") String token) {
        String username = jwtUtils.getUserNameFromJwtToken(token.substring(7));
        User user = userService.findByUsername(username);

        // Create a response DTO without password
        User responseUser = new User();
        responseUser.setId(user.getId());
        responseUser.setUsername(user.getUsername());
        responseUser.setEmail(user.getEmail());
        responseUser.setFirstName(user.getFirstName());
        responseUser.setLastName(user.getLastName());
        responseUser.setBio(user.getBio());
        responseUser.setLocation(user.getLocation());
        responseUser.setProfilePicture(user.getProfilePicture());
        responseUser.setBackGroundImage(user.getBackGroundImage());
        responseUser.setFirstLogin(user.getFirstLogin());
        responseUser.setProfile(user.getProfile());
        responseUser.setRoles(user.getRoles());
        return ResponseEntity.ok(responseUser);
    }


    @GetMapping("/{username}")
    public ResponseEntity<User> findByUsername(@PathVariable String username) {
        try {
            User user = userService.findByUsername(username);
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<UserSearchDTO>> searchUsers(@RequestParam String q) {
        return ResponseEntity.ok(userService.searchUsers(q));
    }


}
