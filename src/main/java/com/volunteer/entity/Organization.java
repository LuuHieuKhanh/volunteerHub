package com.volunteer.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "organization")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Organization {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "organization_name", nullable = false, unique = true, length = 255)
    private String organizationName;

    @Column(columnDefinition = "NVARCHAR(MAX)")
    private String description;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "volunteer_id", nullable = false, unique = true)
    private Volunteer volunteer;
} 