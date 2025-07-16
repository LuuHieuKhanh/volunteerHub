package com.volunteer.repository;

import com.volunteer.entity.VolunteerDonation;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface VolunteerDonationRepository extends JpaRepository<VolunteerDonation, Long> {
    Optional<VolunteerDonation> findByVolunteer_IdAndDonationEvent_Id(Long volunteerId, Long donationEventId);
    List<VolunteerDonation> findByVolunteer_Id(Long volunteerId);
} 