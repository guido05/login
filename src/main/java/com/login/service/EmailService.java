package com.login.service;

import com.login.dto.ResponseDefaultDto;
import com.login.interfaces.MailSenderPort;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService implements MailSenderPort {

    private final JavaMailSender javaMailSender;

    @Value("${app.url.mail}")
    private String appUrlMail;

    @Value("${app.mail.from}")
    private String from;

    public EmailService(JavaMailSender javaMailSender){
        this.javaMailSender = javaMailSender;
    }

    @Override
    public ResponseDefaultDto sendPasswordResetEmail(String mail, String token) throws MessagingException {
        String resetUrl = appUrlMail + token;

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setFrom(from);
        helper.setTo(mail);
        helper.setSubject("Recupera tu contraseña");
        helper.setText(
                "<p>Hola,</p>" +
                        "<p>Has solicitado restablecer tu contraseña.</p>" +
                        "<p><a href=\"" + resetUrl + "\">Haz clic aquí para resetearla</a></p>" +
                        "<p>Si no lo solicitaste, ignora este correo.</p>",
                true
        );

        javaMailSender.send(message);

        return new ResponseDefaultDto(200, "true", null, "Mail enviado exitosamente");
    }

    //Este se usa para el servicio que esta del lado de login
    @Override
    public void sendResentLink(String mail, String token) throws Exception {
        String resetUrl = appUrlMail + token;

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setFrom(from);
        helper.setTo(mail);
        helper.setSubject("Recupera tu contraseña");
        helper.setText(
                "<p>Hola,</p>" +
                        "<p>Has solicitado restablecer tu contraseña.</p>" +
                        "<p><a href=\"" + resetUrl + "\">Haz clic aquí para resetearla</a></p>" +
                        "<p>Si no lo solicitaste, ignora este correo.</p>",
                true
        );

        javaMailSender.send(message);
    }
}
