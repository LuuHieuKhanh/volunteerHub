package com.volunteer.controller;

import com.volunteer.dto.auth.JwtResponse;
import com.volunteer.dto.auth.LoginRequest;
import com.volunteer.dto.auth.SignupRequest;
import com.volunteer.dto.auth.TokenRefreshRequest;
import com.volunteer.dto.auth.TokenRefreshResponse;
import com.volunteer.service.AuthService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<JwtResponse> signup(@Valid @RequestBody SignupRequest signupRequest) {
        logger.info("Signup endpoint called for email: {}", signupRequest.getEmail());
        JwtResponse jwtResponse = authService.signup(signupRequest);
        return ResponseEntity.ok(jwtResponse);
    }

    @PostMapping("/signin")
    public ResponseEntity<JwtResponse> signin(@Valid @RequestBody LoginRequest loginRequest) {
        logger.info("Signin endpoint called for email: {}", loginRequest.getEmail());
        JwtResponse jwtResponse = authService.signin(loginRequest);
        return ResponseEntity.ok(jwtResponse);
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenRefreshResponse> refresh(@Valid @RequestBody TokenRefreshRequest refreshRequest) {
        logger.info("Token refresh endpoint called");
        // TODO: Implement token refresh logic
        return ResponseEntity.ok(new TokenRefreshResponse("newAccessToken", "newRefreshToken"));
    }
} 