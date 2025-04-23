package com.deephire.Service;

import com.deephire.Models.Media;
import com.deephire.Repositories.MediaRepository;
import com.deephire.Repositories.PostRepository;
import com.deephire.Models.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class PostService {
    @Autowired
    private PostRepository postRepository;

    @Autowired
    private MediaRepository mediaRepository;

//    public Post add(String content, MultipartFile mediaFile) throws IOException {
//        Post post = new Post();
//        post.setContent(content);
//
//        Post savedPost = postRepository.save(post); // Save first to get ID
//
//        if (mediaFile != null && !mediaFile.isEmpty()) {
//            // Save file to disk or cloud
//            String filename = UUID.randomUUID() + "_" + mediaFile.getOriginalFilename();
//            Path path = Paths.get("uploads/" + filename);
//            Files.copy(mediaFile.getInputStream(), path);
//
//            Media media = new Media();
//            media.setUrl("/uploads/" + filename); // accessible via static resources
//            media.setPost(savedPost); // set post relationship
//
//            mediaRepository.save(media); // persist media entity
//        }
//
//        return savedPost;
//    }
    public Post add(Post post) {
        return postRepository.save(post);
    }

    public Post find(Long id) {
        return postRepository.findById(id).orElse(null);
    }

    public void delete(Long id) {
        postRepository.deleteById(id);
    }

    public Post update(Post post) {
        return postRepository.saveAndFlush(post);
    }

    public List<Post> findAll() {
        return postRepository.findAll();
    }
}