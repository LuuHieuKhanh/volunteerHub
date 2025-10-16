package com.volunteer.dto.event;

import com.volunteer.enums.EEventStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CharityEventResponseList {
    private Long id;
    private String pic;
    private String name;
    private String description;
    private String requirement;
    private String todo;
    private String destination;
    private LocalDateTime dateStart;
    private LocalDateTime dateEnd;
    private Long numVolunteerRequire;
    private Long numVolunteerActual;
    private EEventStatus status;

    private OrganizationDto organization;

    private boolean joined;
    private boolean followed;  // 👈 thêm

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class OrganizationDto {
        private Long id;
        private String name;
        private String avatar; // map từ logo
        private String reason;
    }
}