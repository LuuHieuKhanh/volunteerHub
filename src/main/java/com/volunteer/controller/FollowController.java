package com.volunteer.controller;

import com.volunteer.dto.follow.OrganizationFollowResponse;
import com.volunteer.entity.Organization;
import com.volunteer.service.FollowService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/follow")
@RequiredArgsConstructor
public class FollowController {
    @Autowired
    private FollowService followService;

    @PostMapping("/{volunteerId}/{organizationId}")
    public ResponseEntity<?> followOrganization(
            @PathVariable("volunteerId") Long volunteerId,
            @PathVariable("organizationId") Long organizationId
    ) {
        followService.followOrganization(volunteerId, organizationId);
        return ResponseEntity.ok("Followed successfully");
    }

    @DeleteMapping("/{volunteerId}/{organizationId}")
    public ResponseEntity<?> unfollowOrganization(
            @PathVariable("volunteerId") Long volunteerId,
            @PathVariable("organizationId") Long organizationId
    ) {
        followService.unfollowOrganization(volunteerId, organizationId);
        return ResponseEntity.ok("Unfollowed successfully");
    }

    @GetMapping("/volunteer/{volunteerId}")
    public ResponseEntity<?> getOrganizationsByVolunteer(@PathVariable Long volunteerId) {
        List<OrganizationFollowResponse> organizations = followService.getOrganizationsFollowedByVolunteer(volunteerId);
        return ResponseEntity.ok(organizations);
    }

}