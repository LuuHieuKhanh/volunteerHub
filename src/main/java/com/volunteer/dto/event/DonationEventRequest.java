package com.volunteer.dto.event;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class DonationEventRequest {
    @NotNull
    private Long organizationId;

    @NotBlank
    private String title;

    private String description;

    @NotNull
    @DecimalMin("0.01")
    private BigDecimal moneyNeed;

    private String note;
    private MultipartFile qrPic;   // ✅ upload file QR
    private MultipartFile pic;

    @NotNull
    private String bankAccount;

    @NotNull
    private LocalDateTime dateStart;

    @NotNull
    private LocalDateTime dateEnd;
} 