package com.volunteer.repository;

import com.volunteer.entity.Follow;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FollowRepository extends JpaRepository<Follow, Long> {
    boolean existsByVolunteerIdAndOrganizationId(Long volunteerId, Long organizationId);
    List<Follow> findByVolunteerId(Long volunteerId);
    List<Follow> findByOrganizationId(Long organizationId);
}
