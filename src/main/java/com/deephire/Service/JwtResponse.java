package com.deephire.Service;

import java.util.List;

public class JwtResponse {

    private String token;

    private String type = "Bearer";

    private String RefreshToken;

    private Long id;

    private String username;

    private String email;

    private List<String> roles;

    public JwtResponse(String accessToken, String refreshToken, Long id, String username,
                       String email, List<String> roles) {

        this.token = accessToken;
        this.RefreshToken = refreshToken;
        this.id = id;
        this.username = username;
        this.email = email;
        this.roles = roles;
    }
}
