package com.volunteer.service;

import com.volunteer.dto.follow.OrganizationFollowResponse;
import com.volunteer.entity.Follow;
import com.volunteer.entity.Organization;
import com.volunteer.entity.Volunteer;
import com.volunteer.repository.FollowRepository;
import com.volunteer.repository.OrganizationRepository;
import com.volunteer.repository.VolunteerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FollowService {
    private final FollowRepository followRepository;
    private final VolunteerRepository volunteerRepository;
    private final OrganizationRepository organizationRepository;

    private final LocalStorageService localStorageService;

    public void followOrganization(Long volunteerId, Long organizationId) {
        if (followRepository.existsByVolunteerIdAndOrganizationId(volunteerId, organizationId)) {
            throw new RuntimeException("Already followed this organization");
        }

        Volunteer volunteer = volunteerRepository.findById(volunteerId)
                .orElseThrow(() -> new RuntimeException("Volunteer not found"));
        Organization organization = organizationRepository.findById(organizationId)
                .orElseThrow(() -> new RuntimeException("Organization not found"));

        Follow follow = Follow.builder()
                .volunteer(volunteer)
                .organization(organization)
                .build();

        followRepository.save(follow);
    }

    public void unfollowOrganization(Long volunteerId, Long organizationId) {
        Follow follow = followRepository.findAll().stream()
                .filter(f -> f.getVolunteer().getId().equals(volunteerId)
                        && f.getOrganization().getId().equals(organizationId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Follow not found"));
        followRepository.delete(follow);
    }

    public List<OrganizationFollowResponse> getOrganizationsFollowedByVolunteer(Long volunteerId) {
        List<Follow> follows = followRepository.findByVolunteerId(volunteerId);
        return follows.stream()
                .map(f -> OrganizationFollowResponse.builder()
                        .id(f.getOrganization().getId())
                        .organizationName(f.getOrganization().getOrganizationName())
                        .description(f.getOrganization().getDescription())
                        .logo(localStorageService.getFullFileUrl(f.getOrganization().getLogo()))
                        .build())
                .collect(Collectors.toList());
    }
}
