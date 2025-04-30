package com.deephire.Dto;


import lombok.Data;

import java.util.Date;

@Data
public class CertificationDto {
    private String name;
    private String issuer;
    private Date issueDate;
    private Date expirationDate;
}

