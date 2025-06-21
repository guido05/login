package com.login.controller;

import com.login.dto.LoginDto;
import com.login.config.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwt;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, JwtUtil jwt) {
        this.authenticationManager = authenticationManager;
        this.jwt = jwt;
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody LoginDto loginDto){
        UsernamePasswordAuthenticationToken login = new UsernamePasswordAuthenticationToken(loginDto.getUsername(),loginDto.getPassword());
        Authentication authentication = this.authenticationManager.authenticate(login);

        String jwt = this.jwt.create(loginDto.getUsername());

        return ResponseEntity.ok().header(HttpHeaders.AUTHORIZATION, jwt).build();
    }
}
