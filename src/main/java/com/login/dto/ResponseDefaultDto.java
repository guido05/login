package com.login.dto;

public record ResponseDefaultDto(
        int code,
        String success,
        Object data,
        String message
) {
}
