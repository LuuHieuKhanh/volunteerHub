package com.volunteer.dto.request;

import com.volunteer.enums.RequestStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateRequestStatusDto {
    private RequestStatus status;
}
