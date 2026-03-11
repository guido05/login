package com.login.exception;


import com.login.dto.ResponseDefaultDto;
import lombok.Getter;


@Getter
public class RuntimeExceptionDTO extends RuntimeException{

    private final ResponseDefaultDto response;

    public RuntimeExceptionDTO(ResponseDefaultDto response) {
        this.response = response;
    }

}
