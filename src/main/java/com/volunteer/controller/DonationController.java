package com.volunteer.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/donations")
public class DonationController {
    // List all donation campaigns
    @GetMapping("")
    public ResponseEntity<List<?>> getAllDonations() {
        // TODO: Return all donation campaigns
        return ResponseEntity.ok(Collections.singletonList(""));
    }

    // Get donation campaign details
    @GetMapping("/{donationId}")
    public ResponseEntity<?> getDonationDetails(@PathVariable Long donationId) {
        // TODO: Return donation campaign details
        return ResponseEntity.ok("Donation details for " + donationId);
    }
} 