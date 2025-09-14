package com.volunteer.repository;

import com.volunteer.entity.DonationEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DonationEventRepository extends JpaRepository<DonationEvent, Long> {

    List<DonationEvent> findByOrganization_Id(Long organizationId);

    List<DonationEvent> findByTitleContainingIgnoreCaseOrOrganization_OrganizationNameContainingIgnoreCase(String title, String organizationName);
}
