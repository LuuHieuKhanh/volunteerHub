package com.volunteer.dto.auth;

import lombok.Data;

@Data
public class JwtResponse {
    private String accessToken;
    private String refreshToken;
    private String tokenType = "Bearer";
    private Long id;
    private Long accountId;
    private String username;
    private String email;
    private String role;

    public JwtResponse(String accessToken, String refreshToken, Long id, Long accountId, String username, String email, String role) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.id = id;
        this.accountId = accountId;
        this.username = username;
        this.email = email;
        this.role = role;
    }
} 