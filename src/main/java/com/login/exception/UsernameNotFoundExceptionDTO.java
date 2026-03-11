package com.login.exception;

import com.login.dto.ResponseDefaultDto;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class UsernameNotFoundExceptionDTO extends UsernameNotFoundException {

    private final ResponseDefaultDto response;

    public UsernameNotFoundExceptionDTO(String msg, ResponseDefaultDto response) {
        super(msg);
        this.response = response;
    }

    public UsernameNotFoundExceptionDTO(String msg, Throwable cause, ResponseDefaultDto response) {
        super(msg, cause);
        this.response = response;
    }
}
