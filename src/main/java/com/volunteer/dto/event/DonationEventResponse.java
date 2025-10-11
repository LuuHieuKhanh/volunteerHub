package com.volunteer.dto.event;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class DonationEventResponse {
    private Long id;
    private Long organizationId;
    private String organizationName;
    private String title;
    private String description;
    private BigDecimal moneyNeed;
    private String eventStatus;
    private boolean hasDonate;
    private String note;
    private String qrPic;
    private String bankAccount;
    private String pic;
    private LocalDateTime dateStart;
    private LocalDateTime dateEnd;
    private BigDecimal totalDonated;

    public DonationEventResponse() {}

    public DonationEventResponse(Long id, Long organizationId, String organizationName, String title, String description, BigDecimal moneyNeed, String eventStatus, boolean hasDonate, String note, String qrPic, String bankAccount, String pic, LocalDateTime dateStart, LocalDateTime dateEnd, BigDecimal totalDonated) {
        this.id = id;
        this.organizationId = organizationId;
        this.organizationName = organizationName;
        this.title = title;
        this.description = description;
        this.moneyNeed = moneyNeed;
        this.eventStatus = eventStatus;
        this.hasDonate = hasDonate;
        this.note = note;
        this.qrPic = qrPic;
        this.bankAccount = bankAccount;
        this.pic = pic;
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
        this.totalDonated = totalDonated;
    }
} 