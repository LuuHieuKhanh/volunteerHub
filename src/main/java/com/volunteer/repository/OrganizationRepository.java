package com.volunteer.repository;

import com.volunteer.entity.Organization;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface OrganizationRepository extends JpaRepository<Organization, Long> {
    Optional<Organization> findByVolunteer_Id(Long volunteerId);

    Optional<Organization> findByOrganizationName(String organizationName);
}