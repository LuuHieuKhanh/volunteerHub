package com.volunteer.entity;

import com.volunteer.enums.EJoinStatus;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "volunteer_charity_event", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"volunteer_id", "charity_event_id"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VolunteerCharityEvent extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "volunteer_id", nullable = false)
    private Volunteer volunteer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "charity_event_id", nullable = false)
    private CharityEvent charityEvent;

    @Enumerated(EnumType.STRING)
    @Column(name = "join_status", nullable = false, length = 50)
    private EJoinStatus joinStatus = EJoinStatus.REGISTERED;
}