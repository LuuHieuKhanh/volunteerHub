package com.volunteer.repository;

import com.volunteer.entity.VolunteerCharityEvent;
import com.volunteer.entity.CharityEvent;
import com.volunteer.entity.Volunteer;
import com.volunteer.enums.EJoinStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface VolunteerCharityEventRepository extends JpaRepository<VolunteerCharityEvent, Long> {
    Optional<VolunteerCharityEvent> findByVolunteerAndCharityEvent(Volunteer volunteer, CharityEvent charityEvent);
    List<VolunteerCharityEvent> findByVolunteer_IdAndJoinStatus(Long volunteerId, EJoinStatus joinStatus);
    List<VolunteerCharityEvent> findByCharityEvent_IdAndJoinStatus(Long charityEventId, EJoinStatus joinStatus);
    boolean existsByVolunteerIdAndCharityEventId(Long volunteerId, Long charityEventId);

    boolean existsByVolunteerAndCharityEvent(Volunteer volunteer, CharityEvent charityEvent);
}