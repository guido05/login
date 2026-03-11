package com.login.dto;

import jakarta.validation.constraints.NotBlank;

public record ResetPasswordRequestDto(
        @NotBlank String token,
        @NotBlank String newPassword
) {
}
