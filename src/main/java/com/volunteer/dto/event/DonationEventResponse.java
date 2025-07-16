package com.volunteer.dto.event;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class DonationEventResponse {
    private Long id;
    private Long organizationId;
    private String title;
    private String description;
    private BigDecimal moneyNeed;
    private String eventStatus;
    private boolean hasDonate;
    private String note;
    private String qrPic;
    private String bankAccount;
    private String pic;

    public DonationEventResponse() {}

    public DonationEventResponse(Long id, Long organizationId, String title, String description, BigDecimal moneyNeed, String eventStatus, boolean hasDonate, String note, String qrPic, String bankAccount, String pic) {
        this.id = id;
        this.organizationId = organizationId;
        this.title = title;
        this.description = description;
        this.moneyNeed = moneyNeed;
        this.eventStatus = eventStatus;
        this.hasDonate = hasDonate;
        this.note = note;
        this.qrPic = qrPic;
        this.bankAccount = bankAccount;
        this.pic = pic;
    }
} 