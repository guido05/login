package com.login.dto;

import jakarta.validation.constraints.NotBlank;

public record ChangePasswordDto(
        @NotBlank String username,
        @NotBlank String oldPassword,
        @NotBlank String newPassword
) {
}
