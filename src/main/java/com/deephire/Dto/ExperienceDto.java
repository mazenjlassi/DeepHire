package com.deephire.Dto;

import lombok.Data;

import java.util.Date;

@Data

public class ExperienceDto {

    private String companyName;

    private String title;

    private Date startDate;

    private Date endDate;

    private String description;
}
