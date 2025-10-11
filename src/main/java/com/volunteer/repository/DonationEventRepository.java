package com.volunteer.repository;

import com.volunteer.entity.DonationEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface DonationEventRepository extends JpaRepository<DonationEvent, Long> {
    long countByOrganizationId(Long organizationId);
    List<DonationEvent> findByOrganization_Id(Long organizationId);

    @Query("""
    SELECT d FROM DonationEvent d
    WHERE d.organization.id = :organizationId
      AND (:title IS NULL OR LOWER(d.title) LIKE LOWER(CONCAT('%', :title, '%')))
      AND (:fromDateTime IS NULL OR d.dateStart >= :fromDateTime)
      AND (:toDateTime IS NULL OR d.dateEnd <= :toDateTime)
""")
    List<DonationEvent> searchDonationsByOrganization(
            @Param("organizationId") Long organizationId,
            @Param("title") String title,
            @Param("fromDateTime") LocalDateTime fromDateTime,
            @Param("toDateTime") LocalDateTime toDateTime
    );

    List<DonationEvent> findByTitleContainingIgnoreCaseOrOrganization_OrganizationNameContainingIgnoreCase(String title, String organizationName);
} 