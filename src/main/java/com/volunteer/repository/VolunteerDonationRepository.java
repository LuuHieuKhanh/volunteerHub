package com.volunteer.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.volunteer.entity.VolunteerDonation;

public interface VolunteerDonationRepository extends JpaRepository<VolunteerDonation, Long> {

    Optional<VolunteerDonation> findByVolunteer_IdAndDonationEvent_Id(Long volunteerId, Long donationEventId);

    List<VolunteerDonation> findByVolunteer_Id(Long volunteerId);

    List<VolunteerDonation> findByDonationEventId(Long donationEventId);
}
