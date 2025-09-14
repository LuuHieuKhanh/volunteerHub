package com.volunteer.dto.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VolunteerDonationResponse {
    private Long id;
    private String fullName;
    private String email;
    private String contact;
    private BigDecimal donateAmount;
    private LocalDateTime donateTime;
    private String note;
}