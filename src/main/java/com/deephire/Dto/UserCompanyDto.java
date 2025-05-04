package com.deephire.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserCompanyDto {
    private String email;
    private String firstName;
    private String lastName;
    private String logo;
    private String CompanyName;
    private String industry;
    private String location;
    private String description;
}
