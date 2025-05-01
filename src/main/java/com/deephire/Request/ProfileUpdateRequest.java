package com.deephire.Request;

import lombok.Data;

@Data
public class ProfileUpdateRequest {
    private String firstName;
    private String lastName;
    private String bio;
    private String location;
    private String headline;
    private String summary;
}