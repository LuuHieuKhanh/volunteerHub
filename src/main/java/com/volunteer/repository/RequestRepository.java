package com.volunteer.repository;

import com.volunteer.entity.Request;
import com.volunteer.enums.RequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface RequestRepository extends JpaRepository<Request, Long> {
    List<Request> findByVolunteer_Id(Long volunteerId);
    List<Request> findByStatus(RequestStatus status);
} 