package com.volunteer.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "volunteer_donation", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"volunteer_id", "donation_event_id"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VolunteerDonation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "volunteer_id", nullable = false)
    private Volunteer volunteer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "donation_event_id", nullable = false)
    private DonationEvent donationEvent;

    @Column(name = "donate_amount", nullable = false, precision = 19, scale = 2)
    private BigDecimal donateAmount;

    @Column(name = "donation_date", nullable = false)
    private LocalDateTime donationDate = LocalDateTime.now();

    @Column(columnDefinition = "NVARCHAR(MAX)")
    private String note;
} 