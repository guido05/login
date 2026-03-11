package com.login.repository;

import com.login.entity.ResetPasswordToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ResetPasswordTokenRepository extends JpaRepository<ResetPasswordToken,String> {

    Optional<ResetPasswordToken> findByTokenAndUsedFalse(String token);
}
