package com.login.dto;

import java.util.List;

public record RegisterUserDto(
        String username,
        String password,
        String email,
        List<String> roles
) {
}
