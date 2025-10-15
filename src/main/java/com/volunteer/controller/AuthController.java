package com.volunteer.controller;

import com.volunteer.dto.auth.*;
import com.volunteer.dto.volunteer.VolunteerResponse;
import com.volunteer.entity.Volunteer;
import com.volunteer.repository.VolunteerRepository;
import com.volunteer.security.jwt.JwtUtils;
import com.volunteer.security.services.UserDetailsServiceImple;
import com.volunteer.service.AuthService;
import com.volunteer.service.VolunteerService;
import io.jsonwebtoken.Claims;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private AuthService authService;
    private VolunteerService  volunteerService;
    private JwtUtils jwtUtils;

    @Autowired
    private PasswordResetService passwordResetService;

    public AuthController(JwtUtils jwtUtils, VolunteerService volunteerService) {
        this.jwtUtils = jwtUtils;
        this.volunteerService = volunteerService;
    }

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

    @GetMapping("/authenticated")
    public ResponseEntity<?> authenticated(@RequestHeader("Authorization") String authHeader) {
        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing or invalid Authorization header");
            }

            String token = authHeader.substring(7); // Bỏ "Bearer "
            Claims claims = jwtUtils.decodeJwt(token);
            if (claims == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
            }
            logger.info("Claims: {}", claims);
            String email = claims.getSubject(); // lấy từ .setSubject(...) khi tạo token

            VolunteerResponse volunteer = volunteerService.getVolunteerByEmail(email, true);
            return ResponseEntity.ok(volunteer);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenRefreshResponse> refresh(@Valid @RequestBody TokenRefreshRequest refreshRequest) {
        logger.info("Token refresh endpoint called");
        // TODO: Implement token refresh logic
        return ResponseEntity.ok(new TokenRefreshResponse("newAccessToken", "newRefreshToken"));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<Map<String, String>> resetPassword(@Valid @RequestBody ResetPasswordRequest resetPasswordRequest) {
        logger.info("Reset password endpoint called for email: {}", resetPasswordRequest.getEmail());

        try {
            passwordResetService.resetPassword(resetPasswordRequest.getEmail());

            Map<String, String> response = new HashMap<>();
            response.put("message", "New password has been sent to your email address");
            response.put("status", "success");

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error resetting password for email: {}", resetPasswordRequest.getEmail(), e);

            Map<String, String> response = new HashMap<>();
            response.put("message", "Failed to reset password. Please try again later.");
            response.put("status", "error");

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
} 