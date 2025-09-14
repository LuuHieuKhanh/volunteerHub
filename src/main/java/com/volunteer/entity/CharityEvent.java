package com.volunteer.entity;

import com.volunteer.enums.EEventStatus;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "charity_events")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CharityEvent extends BaseEntity {
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

    @Column(columnDefinition = "NVARCHAR(MAX)")
    private String todo;

    @Column(columnDefinition = "NVARCHAR(MAX)")
    private String requirement;

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

    @Enumerated(EnumType.STRING)
    @Column(name = "event_status", nullable = false, length = 50)
    private EEventStatus eventStatus = EEventStatus.UPCOMING;

    @OneToMany(mappedBy = "charityEvent")
    private List<VolunteerCharityEvent> participants;

    public void increaseVolunteerCount() {
        if (this.numVolunteerActual == null) {
            this.numVolunteerActual = 0L;
        }
        this.numVolunteerActual++;
    }

    public void decreaseVolunteerCount() {
        if (this.numVolunteerActual == null) {
            this.numVolunteerActual = 0L;
        }
        if (this.numVolunteerActual > 0) {
            this.numVolunteerActual--;
        }
    }
} 