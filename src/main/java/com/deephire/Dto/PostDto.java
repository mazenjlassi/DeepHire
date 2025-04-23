package com.deephire.Dto;

import com.deephire.Models.Comment;
import com.deephire.Models.Media;
import lombok.*;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostDto {
    private Long id;
    private String content;
    private Date timestamp;
    private int likes;
    private List<Comment> comments;
    private List<Media> media;
    private UserSummaryDto user;
}

