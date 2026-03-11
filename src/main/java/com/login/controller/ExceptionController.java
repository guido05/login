package com.login.controller;

import com.login.dto.ResponseDefaultDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionController {

    /*@ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseDefaultDto> handleGeneric(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ResponseDefaultDto(500, "Error", null, ex.getMessage()));
    }*/

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ResponseDefaultDto> handleBadCredentials(BadCredentialsException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ResponseDefaultDto(401, "Unauthorized", null, "Usuario o contraseña inválidos"));
    }
}
