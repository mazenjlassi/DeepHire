package com.deephire.Dto;

import lombok.Data;

import java.util.Date;



@Data

public class EducationDto {


    private String schoolName;
    private String degree;
    private String fieldOfStudy;
    private Date startDate;
    private Date endDate;
}
