package com.volunteer.repository;

import com.volunteer.entity.CharityEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface CharityEventRepository extends JpaRepository<CharityEvent, Long> {

    List<CharityEvent> findByOrganization_Id(Long organizationId);

    @Query("""
    SELECT e FROM CharityEvent e
    WHERE e.organization.id = :organizationId
    AND (:name IS NULL OR LOWER(e.charityName) LIKE LOWER(CONCAT('%', :name, '%')))
    AND (:from IS NULL OR e.dateStart >= :from)
    AND (:to IS NULL OR e.dateEnd <= :to)
""")
    List<CharityEvent> searchCharitiesByOrganization(
            @Param("organizationId") Long organizationId,
            @Param("name") String name,
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to
    );

    List<CharityEvent> findByCharityNameContainingIgnoreCaseOrOrganization_OrganizationNameContainingIgnoreCase(String charityName, String organizationName);
}
