package com.volunteer.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "charity_events")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CharityEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organization_id", nullable = false)
    private Organization organization;

    @Column(nullable = false, length = 255)
    private String charityName;

    @Column(columnDefinition = "NVARCHAR(MAX)")
    private String description;

    @Column(length = 255)
    private String destination;

    @Column(name = "date_start", nullable = false)
    private LocalDateTime dateStart;

    @Column(name = "date_end", nullable = false)
    private LocalDateTime dateEnd;

    @Column(name = "num_volunteer_require", nullable = false)
    private Long numVolunteerRequire;

    @Column(name = "num_volunteer_actual")
    private Long numVolunteerActual;

    @Column(columnDefinition = "NVARCHAR(MAX)")
    private String note;

    @Column(columnDefinition = "NVARCHAR(MAX)")
    private String pic;

    @Column(name = "event_status", nullable = false, length = 50)
    private String eventStatus;
} 