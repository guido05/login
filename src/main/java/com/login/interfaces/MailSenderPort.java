package com.login.interfaces;

import com.login.dto.ResponseDefaultDto;
import jakarta.mail.MessagingException;

public interface MailSenderPort {

    void sendResentLink(String mail, String token) throws Exception;

    ResponseDefaultDto sendPasswordResetEmail(String mail, String token) throws MessagingException;
}
