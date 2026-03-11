package com.login.service;

import com.login.dto.ResponseDefaultDto;
import com.login.entity.ResetPasswordToken;
import com.login.entity.UserEntity;
import com.login.entity.UserRoleEntity;
import com.login.exception.RuntimeExceptionDTO;
import com.login.interfaces.MailSenderPort;
import com.login.repository.ResetPasswordTokenRepository;
import com.login.repository.UserRepository;
import com.login.repository.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;
    private final ResetPasswordTokenRepository tokenRepo;

    private final MailSenderPort emailService;

    @Value("${app.reset.token.minutes:15}")
    private int tokenMinutes;

    public void registerUser(String username, String password, String email,List<String> roles) {
        if (userRepository.existsById(username)) {
            throw new RuntimeExceptionDTO(new ResponseDefaultDto(403,"Error",null,"El usuario ya existe"));
        }

        UserEntity user = new UserEntity();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setMail(email);
        user.setLocked(false);
        user.setDisabled(false);

        List<UserRoleEntity> roleEntities = roles.stream()
                .map(role -> new UserRoleEntity(user, role))
                .toList();

        //user.setRoles(roleEntities);
        userRepository.save(user);

        for(String rol: roles){
            UserRoleEntity userRoleEntity = new UserRoleEntity(user,rol);
            userRoleRepository.save(userRoleEntity);
        }
    }

    public void changePassword(String username,String oldPassword,String newPassword){
        UserEntity userEntity = userRepository.findById(username).orElseThrow(() ->
                new RuntimeExceptionDTO(new ResponseDefaultDto(403,"Error",null,"Usuario no encontrado")));
        if (!passwordEncoder.matches(oldPassword, userEntity.getPassword())){
            throw  new RuntimeExceptionDTO(new ResponseDefaultDto(403,"Error",null,"La contraseña actual es incorrecta"));
        }

        userEntity.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(userEntity);
    }

    public void forgotPassword(String username,String newPassword){
        UserEntity userEntity = userRepository.findById(username).orElseThrow(() ->
                new RuntimeExceptionDTO(new ResponseDefaultDto(403,"Error",null,"Usuario no encontrado")));

        userEntity.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(userEntity);
    }

    /** Paso 1: generar token + enviar mail con link */
    @Transactional
    public void startForgotPassword(String usernameOrEmail) throws Exception {
        // Buscá por username; si no, por email
        UserEntity user = userRepository.findById(usernameOrEmail)
                        .orElseGet(() -> userRepository.findByMail(usernameOrEmail)
                        .orElseThrow(() -> new RuntimeExceptionDTO(new ResponseDefaultDto(403,"Error",null,"Usuario no encontrado"))));

        String token = UUID.randomUUID().toString();

        ResetPasswordToken t = new ResetPasswordToken();
        t.setToken(token);
        t.setUsername(user.getUsername());
        t.setExpiresAt(LocalDateTime.now().plusMinutes(tokenMinutes));
        tokenRepo.save(t);

        emailService.sendResentLink(user.getMail(), token);
    }

    /** Paso 2: validar token + setear nueva password */
    @Transactional
    public void confirmResetPassword(String token, String newPassword) {
        ResetPasswordToken t = tokenRepo.findByTokenAndUsedFalse(token)
                .orElseThrow(() -> new RuntimeExceptionDTO(new ResponseDefaultDto(403,"Error",null,"Token inválido")));

        if (t.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new RuntimeExceptionDTO(new ResponseDefaultDto(403,"Error",null,"Token vencido"));
        }

        UserEntity user = userRepository.findById(t.getUsername())
                .orElseThrow(() -> new RuntimeExceptionDTO(new ResponseDefaultDto(403,"Error",null,"Usuario no encontrado")));

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        t.setUsed(true);
        tokenRepo.save(t);
    }

    public void validateToken(String token) {
        var t = tokenRepo.findByTokenAndUsedFalse(token)
                .orElseThrow(() ->new RuntimeExceptionDTO(new ResponseDefaultDto(403,"Error",null,"Token inválido")));
        if (t.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new RuntimeExceptionDTO(new ResponseDefaultDto(403,"Error",null,"Token vencido"));
        }
    }
}
