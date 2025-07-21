package com.volunteer.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "volunteer")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Volunteer extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100)
    private String fullName;

    @Column(columnDefinition = "NVARCHAR(MAX)")
    private String pic;

    @Column(length = 20)
    private String contact;

    @Column(name = "is_banned", nullable = false)
    private boolean isBanned = false;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false, unique = true)
    private Account account;

    @OneToMany(mappedBy = "volunteer")
    private List<VolunteerCharityEvent> joinedEvents;

    @OneToMany(mappedBy = "volunteer")
    private List<VolunteerDonation> donations;

}