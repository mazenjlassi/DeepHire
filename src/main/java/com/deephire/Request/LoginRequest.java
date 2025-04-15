package com.deephire.Request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LoginRequest {
    // Getter et Setter pour `username`
    @NotBlank
    private String username;
    // Getter et Setter pour `password`
    @NotBlank
    private String password;

}
