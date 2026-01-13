package com.volunteer.repository;

import com.volunteer.entity.Request;
import com.volunteer.entity.Organization;
import com.volunteer.enums.RequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface RequestRepository extends JpaRepository<Request, Long> {

    List<Request> findByVolunteer_Id(Long volunteerId);

    Optional<Request> findByVolunteer_IdAndStatusIn(Long volunteerId, Collection<RequestStatus> statuses);

    List<Request> findByStatus(RequestStatus status);

    boolean existsByOrganizationAndStatus(Organization organization, RequestStatus status);

    @Query("SELECT r FROM Request r "
            + "LEFT JOIN FETCH r.volunteer "
            + "LEFT JOIN FETCH r.organization "
            + "WHERE r.volunteer.id = :volunteerId")
    List<Request> findAllByVolunteerIdWithDetails(@Param("volunteerId") Long volunteerId);

    List<Request> findByOrganization_OrganizationNameContainingIgnoreCase(String organizationName);

    Long countByStatus(RequestStatus status);

    @Query("""
SELECT r
FROM Request r
WHERE r.organization.id = :volunteerId
  AND r.requestType = 'ORGANIZATION_REGISTRATION'
  AND r.status = 'APPROVED'
""")
    List<Request> findCharityEventsByVolunteerAndRequestType(@Param("volunteerId") Long volunteerId);

}
