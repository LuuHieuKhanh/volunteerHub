package com.volunteer.dto.volunteer;

import com.volunteer.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class VolunteerUpdateRequest {

    @Email
    private String email;

    @Size(min = 6, max = 40)
    private String password;

    @Size(min = 3, max = 50)
    private String fullName;

    private String contact;

    private Role role;

    private Boolean isActive;

    private Boolean isBanned;
}
