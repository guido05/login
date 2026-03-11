package com.login.controller;

import com.login.dto.*;
import com.login.config.JwtUtil;
import com.login.service.UserService;
import jakarta.validation.Valid;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwt;
    private final UserService userService;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, JwtUtil jwt, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwt = jwt;
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseDefaultDto> login(@RequestBody LoginDto loginDto){
        UsernamePasswordAuthenticationToken login = new UsernamePasswordAuthenticationToken(loginDto.username(), loginDto.password());
        Authentication authentication = this.authenticationManager.authenticate(login);

        String jwt = this.jwt.create(loginDto.username());

        List<String> roles = authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
        return ResponseEntity.ok(new ResponseDefaultDto(200,"Successful",new ResponseDto(loginDto.username(), roles, jwt),"Conectado correctamente"));
    }

    @PostMapping("/register-user")
    public ResponseEntity<ResponseDefaultDto> registerUser(@RequestBody RegisterUserDto registerUserDto){
        userService.registerUser(registerUserDto.username(),registerUserDto.password(), registerUserDto.email(), registerUserDto.roles());
        return ResponseEntity.ok(new ResponseDefaultDto(200,"Successful",null,"Usuario creado"));
    }

    @PostMapping("/change-password")
    public ResponseEntity<ResponseDefaultDto> changePassword(@Valid @RequestBody ChangePasswordDto changePasswordDto){
        userService.changePassword(changePasswordDto.username(),changePasswordDto.oldPassword(), changePasswordDto.newPassword());
        return ResponseEntity.ok(new ResponseDefaultDto(200,"Successful",null,"Contraseña cambiada"));
    }

    /** Paso 1: el usuario envía username o email y recibe un mail con el link */
    @PostMapping("/forgot-password")
    public ResponseEntity<ResponseDefaultDto> forgotPassword(@Valid @RequestBody ForgotPasswordDto changePasswordDto) throws Exception {
        userService.startForgotPassword(changePasswordDto.mail());
        return ResponseEntity.ok(new ResponseDefaultDto(200,"Successful",null,"Correo enviado"));
    }

    /** Paso 2: desde el form de reset, envía token + nueva contraseña */
    @PostMapping("/reset-password")
    public ResponseEntity<ResponseDefaultDto> resetPassword(@Valid @RequestBody ResetPasswordRequestDto req) {
        userService.confirmResetPassword(req.token(), req.newPassword());
        return ResponseEntity.ok(new ResponseDefaultDto(200, "OK", null, "Contraseña actualizada.")
        );
    }

    /** (Opcional) Validar token antes de mostrar el form en el front */
    @GetMapping("/reset-password")
    public ResponseEntity<ResponseDefaultDto> validateToken(@RequestParam String token) {
        userService.validateToken(token);
        return ResponseEntity.ok(new ResponseDefaultDto(200, "OK", null, "Token válido."));
    }
}
