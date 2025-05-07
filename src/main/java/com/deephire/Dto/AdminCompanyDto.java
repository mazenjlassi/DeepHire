package com.deephire.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
    public class AdminCompanyDto {
        private Long idCompany;
        private String name;
        private String industry;
        private String location;
        private String file;
        private String logo;
        private String email;
        private String firstName;
        private String lastName;
        private String status;
    }
