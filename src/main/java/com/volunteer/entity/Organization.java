package com.volunteer.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "organizations")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Organization extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "organization_name", columnDefinition = "NVARCHAR(MAX)", nullable = false, unique = true, length = 255)
    private String organizationName = "";

    @Column(columnDefinition = "NVARCHAR(MAX)", nullable = false)
    private String description = "";

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "volunteer_id", nullable = false, unique = true)
    private Volunteer volunteer;

    @Column(name = "certificate_pic", columnDefinition = "NVARCHAR(MAX)")
    private String certificate = "";

    @Column(name = "logo_pic", columnDefinition = "NVARCHAR(MAX)")
    private String logo = "";

    @OneToMany(mappedBy = "organization", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Follow> followers = new ArrayList<>();
} 