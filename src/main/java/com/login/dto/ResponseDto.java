package com.login.dto;

import java.util.List;

public record ResponseDto (
        String username,
        List<String> rolUser,
        String token
){}
