package com.volunteer.controller;

import com.volunteer.dto.volunteer.ChangePasswordRequest;
import com.volunteer.dto.volunteer.VolunteerUpdateRequest;
import com.volunteer.entity.Volunteer;
import com.volunteer.service.AccountService;
import com.volunteer.service.VolunteerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/profile")
public class ProfileController {
    @Autowired
    private VolunteerService volunteerService;
    @Autowired
    private AccountService accountService;
    // View user profile
    @GetMapping("")
    public ResponseEntity<?> getProfile() {
        // TODO: Return user profile
        return ResponseEntity.ok("User profile");
    }

    // Update user profile
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Volunteer> updateVolunteer(
            @PathVariable("id") Long id,
            @Valid @RequestPart("data") VolunteerUpdateRequest request,
            @RequestPart(value = "pic", required = false) MultipartFile pic
    ) throws IOException {
        Volunteer updated = volunteerService.updateVolunteerUser(id, request, pic);
        return ResponseEntity.ok(updated);
    }

    @PutMapping("/{id}/password")
    public ResponseEntity<?> changePassword(
            @PathVariable("id") Long id,
            @Valid @RequestBody ChangePasswordRequest request
    ) {
        accountService.changePassword(id, request);
        return ResponseEntity.ok("Password changed successfully");
    }
} 