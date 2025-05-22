package com.deephire.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobApplicationDTO {
    private Long id;
    private String title;
    private String company;
    private String location;
    private Date datePosted;
    private Status status;
    private UserDTO user;

    public enum Status {
        PENDING, APPROVED, REJECTED
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserDTO {
        private Long id;
        private String username;
        private String email;
    }
}
