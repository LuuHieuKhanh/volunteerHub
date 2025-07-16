package com.volunteer.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "donation_events")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DonationEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organization_id", nullable = false)
    private Organization organization;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(columnDefinition = "NVARCHAR(MAX)")
    private String description;

    @Column(name = "money_need", nullable = false, precision = 19, scale = 2)
    private BigDecimal moneyNeed;

    @Column(name = "event_status", nullable = false, length = 50)
    private String eventStatus;

    @Column(name = "has_donate", nullable = false)
    private boolean hasDonate = false;

    @Column(columnDefinition = "NVARCHAR(MAX)")
    private String note;

    @Column(name = "qr_pic", columnDefinition = "NVARCHAR(MAX)")
    private String qrPic;

    @Column(name = "bank_account", length = 255)
    private String bankAccount;

    @Column(columnDefinition = "NVARCHAR(MAX)")
    private String pic;
} 