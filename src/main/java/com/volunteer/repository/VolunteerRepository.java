package com.volunteer.repository;

import com.volunteer.entity.Volunteer;
import com.volunteer.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface VolunteerRepository extends JpaRepository<Volunteer, Long> {
    Optional<Volunteer> findByUsernameAndIsActiveTrue(String username);
    Optional<Volunteer> findByEmailAndIsActiveTrue(String email);
    List<Volunteer> findByAccount_RoleAndIsActiveTrue(Role role);
    List<Volunteer> findByIsActiveTrue();
} 