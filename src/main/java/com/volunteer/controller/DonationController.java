package com.volunteer.controller;

import com.volunteer.dto.donation.VolunteerDonationRequest;
import com.volunteer.entity.VolunteerDonation;
import com.volunteer.service.VolunteerDonationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/donations")
public class DonationController {
    @Autowired
    private VolunteerDonationService volunteerDonationService;
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

    @PostMapping
    public ResponseEntity<?> createDonation(@RequestBody VolunteerDonationRequest request) {
        VolunteerDonation donation = volunteerDonationService.createDonation(request);
        return ResponseEntity.ok("Donation Success");
    }
} 