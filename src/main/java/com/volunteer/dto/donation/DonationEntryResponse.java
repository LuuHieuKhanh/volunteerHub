package com.volunteer.dto.donation;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class DonationEntryResponse {

    private Long id;
    private Long volunteerId;
    private Long donationEventId;
    private BigDecimal donateAmount;
    private LocalDateTime donationDate;
    private LocalDateTime createdAt;
    private String note;
    private String donationEventTitle;

    public DonationEntryResponse() {
    }

    public DonationEntryResponse(Long id, Long volunteerId, Long donationEventId, BigDecimal donateAmount, LocalDateTime donationDate, String note) {
        this.id = id;
        this.volunteerId = volunteerId;
        this.donationEventId = donationEventId;
        this.donateAmount = donateAmount;
        this.donationDate = donationDate;
        this.note = note;
    }
}
