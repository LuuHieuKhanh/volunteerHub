package com.volunteer.security;

import org.springframework.stereotype.Component;

@Component
public class VolunteerSecurity {
    // Example: @PreAuthorize("@volunteerSecurity.isOwner(#id)")
    public boolean isOwner(Long id) {
        // TODO: Implement logic to check if current user is owner of resource with given id
        return true;
    }
} 