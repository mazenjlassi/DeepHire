package com.deephire.Dto;

import lombok.Data;

import java.util.Date;

@Data
public class JobPostingRequestDTO {
       private   Long id;
    private String title;
        private String description;
        private String requirements;
        private String location;
        private Date datePosted;

    }