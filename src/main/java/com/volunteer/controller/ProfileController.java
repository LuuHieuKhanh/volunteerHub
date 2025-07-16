package com.volunteer.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profile")
public class ProfileController {
    // View user profile
    @GetMapping("")
    public ResponseEntity<?> getProfile() {
        // TODO: Return user profile
        return ResponseEntity.ok("User profile");
    }

    // Update user profile
    @PutMapping("")
    public ResponseEntity<?> updateProfile(@RequestBody Object profileUpdateRequest) {
        // TODO: Update user profile
        return ResponseEntity.ok("Profile updated");
    }
} 