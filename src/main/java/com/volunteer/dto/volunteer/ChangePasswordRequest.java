package com.volunteer.dto.volunteer;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ChangePasswordRequest {
    @NotBlank
    private String password; // old password

    @NotBlank
    private String newPassword;

    @NotBlank
    private String confirmPassword;
} 