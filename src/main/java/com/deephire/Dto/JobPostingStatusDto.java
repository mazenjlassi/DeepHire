package com.deephire.Dto;

import com.deephire.Enums.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobPostingStatusDto {
    private Long jobPostingId;
    private String jobTitle;
    private String jobLocation;
    private Date datePosted;
    private String companyName;
    private String companyIndustry;
    private String status;  // Added field
}


