package com.deephire.Service;

import com.deephire.Repositories.PostRepository;
import com.deephire.models.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostService {
    @Autowired
    private PostRepository postRepository;

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