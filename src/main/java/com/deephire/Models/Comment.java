package com.deephire.Models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String content;
    private Date timestamp;

    @ManyToOne
    @JsonBackReference("post-comment")
    private Post post;

    @ManyToOne
    @JsonBackReference("user-comment")
    private User user;
}