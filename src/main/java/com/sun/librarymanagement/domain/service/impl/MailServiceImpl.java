package com.sun.librarymanagement.domain.service.impl;

import com.sun.librarymanagement.domain.service.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {

    private final JavaMailSender mailSender;

    @Value("${mail.verification-base-url}")
    private String verificationBaseUrl;

    @Override
    public void sendVerificationEmail(String to, String token) {
        String verificationLink = UriComponentsBuilder
            .fromUriString(verificationBaseUrl)
            .path("/api/v1/users/verify")
            .queryParam("token", token)
            .toUriString();
        String emailContent = "Click the link to verify your account:\n" + verificationLink;
        String emailSubject = "Verify your account";

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(emailSubject);
        message.setText(emailContent);

        mailSender.send(message);
    }
}
