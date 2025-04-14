package com.deephire.Service;

import com.deephire.Repositories.CommentRepository;
import com.deephire.Models.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {
    @Autowired
    private CommentRepository commentRepository;

    public Comment add(Comment comment) {
        return commentRepository.save(comment);
    }

    public Comment find(String id) {
        return commentRepository.findById(id).orElse(null);
    }

    public void delete(String id) {
        commentRepository.deleteById(id);
    }

    public Comment update(Comment comment) {
        return commentRepository.saveAndFlush(comment);
    }

    public List<Comment> findAll() {
        return commentRepository.findAll();
    }
}