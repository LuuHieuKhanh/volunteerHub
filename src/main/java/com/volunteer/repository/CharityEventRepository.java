package com.volunteer.repository;

import com.volunteer.entity.CharityEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CharityEventRepository extends JpaRepository<CharityEvent, Long> {
    List<CharityEvent> findByOrganization_Id(Long organizationId);
} 