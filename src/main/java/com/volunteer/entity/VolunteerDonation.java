package com.volunteer.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
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
public class VolunteerDonation extends BaseEntity{
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
    @DecimalMin(value = "0.01", message = "Donation must be greater than 0")
    private BigDecimal donateAmount;

    @Column(columnDefinition = "NVARCHAR(MAX)")
    private String note;
} 