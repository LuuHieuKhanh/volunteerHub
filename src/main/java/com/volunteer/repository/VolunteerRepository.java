package com.volunteer.repository;

import com.volunteer.entity.Volunteer;
import com.volunteer.enums.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface VolunteerRepository extends JpaRepository<Volunteer, Long> {

    Optional<Volunteer> findByFullName(String username);

    Optional<Volunteer> findByAccount_EmailAndAccount_IsActive(String accountEmail, boolean accountActive);

    Optional<Volunteer> findByAccount_Email(String accountEmail);

    List<Volunteer> findByAccount_Role(Role role);

    List<Volunteer> findByAccount_IsActive(boolean active);

    // Search methods for admin
    Page<Volunteer> findByFullNameContainingIgnoreCaseOrAccount_EmailContainingIgnoreCase(
            String fullName, String email, Pageable pageable);

    List<Volunteer> findByFullNameContainingIgnoreCaseOrAccount_EmailContainingIgnoreCase(
            String fullName, String email);
}
