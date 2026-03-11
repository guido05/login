package com.login.dto;

import jakarta.validation.constraints.NotBlank;

public record ForgotPasswordDto(
        @NotBlank String mail
) {
}
