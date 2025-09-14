package com.volunteer.repository;
import com.volunteer.entity.VolunteerDonation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface VolunteerDonationRepository extends JpaRepository<VolunteerDonation, Long> {
    List<VolunteerDonation> findByVolunteerId(Long volunteerId);
    long countByVolunteerId(Long volunteerId);
    Optional<VolunteerDonation> findByVolunteer_IdAndDonationEvent_Id(Long volunteerId, Long donationEventId);

    List<VolunteerDonation> findByVolunteer_Id(Long volunteerId);

    @Query("SELECT COALESCE(SUM(vd.donateAmount), 0) " +
            "FROM VolunteerDonation vd " +
            "WHERE vd.donationEvent.id = :donationEventId")
    BigDecimal getTotalDonationByEvent(@Param("donationEventId") Long donationEventId);

    List<VolunteerDonation> findByDonationEventId(Long donationEventId);
}
