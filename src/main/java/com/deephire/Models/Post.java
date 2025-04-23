package com.deephire.Models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String content;
    private Date timestamp;
    private int likes;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    @JsonManagedReference("post-comment")
    private List<Comment> comments;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    @JsonManagedReference("post-media")
    private List<Media> media;

    @ManyToOne
    @JsonBackReference("user-post")
    private User user;


}