package com.deephire.Controllers;

import com.deephire.Dto.PostDto;
import com.deephire.Dto.UserSummaryDto;
import com.deephire.JWT.JwtUtils;
import com.deephire.Models.Media;
import com.deephire.Models.User;
import com.deephire.Repositories.UserRepository;
import com.deephire.Request.ProfileCompletionRequest;
import com.deephire.Service.PostService;
import com.deephire.Models.Post;
import com.deephire.Service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/post")
public class PostRestController {

    @Autowired
    private PostService postService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtUtils  jwtUtils;

    @PostMapping("/add")
    public ResponseEntity<Post> add(
            @RequestHeader("Authorization") String token,
            @RequestParam("content") String content,
            @RequestPart(value = "mediaFiles", required = false) List<MultipartFile> mediaFiles) {

        try {
            String username = jwtUtils.getUserNameFromJwtToken(token.substring(7));
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // Create new post
            Post post = new Post();
            post.setContent(content);
            post.setUser(user);
            post.setTimestamp(new Date());
            post.setLikes(0);

            // Process media files if present
            if (mediaFiles != null && !mediaFiles.isEmpty()) {
                List<Media> mediaList = new ArrayList<>();

                for (MultipartFile file : mediaFiles) {
                    if (!file.isEmpty()) {
                        Media media = new Media();
                        media.setFiles(file.getBytes());
                        media.setPost(post);
                        mediaList.add(media);
                    }
                }

                post.setMedia(mediaList);
            }

            // Save the post (which will cascade to media)
            Post savedPost = postService.add(post);

            return new ResponseEntity<>(savedPost, HttpStatus.CREATED);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/all2")
    public ResponseEntity<List<Post>> all2() {
        try {
            List<Post> posts = postService.findAll();
            if (posts.isEmpty()) {
                return new ResponseEntity<>(posts, HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(posts, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    @GetMapping("/findById/{id}")
    public ResponseEntity<Post> findById(@PathVariable Long id) {
        Post post = postService.find(id);
        if (post != null) {
            return new ResponseEntity<>(post, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Post> update(@PathVariable Long id, @RequestBody Post post) {
        Post existingPost = postService.find(id);
        if (existingPost != null) {
            postService.update(post);
            return new ResponseEntity<>(post, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable Long id) {
        try {
            postService.delete(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    @GetMapping("/all")
    public ResponseEntity<List<PostDto>> all() {
        List<Post> posts = postService.findAll();
        List<PostDto> dtos = posts.stream().map(p -> {
            User u = p.getUser();
            UserSummaryDto us = new UserSummaryDto(
                    u.getId(), u.getUsername(), u.getFirstName(), u.getLastName(),
                    Base64.getEncoder().encodeToString(u.getProfilePicture()),null
            );
            return new PostDto(p.getId(), p.getContent(), p.getTimestamp(),
                    p.getLikes(), p.getComments(), p.getMedia(), us);
        }).collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }
}