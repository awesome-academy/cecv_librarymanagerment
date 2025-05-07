package com.sun.librarymanagement.domain.service.impl;

import com.sun.librarymanagement.domain.service.MailService;
import com.sun.librarymanagement.utils.ApiPaths;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {

    private final JavaMailSender mailSender;

    @Value("${mail.verification-base-url}")
    private String verificationBaseUrl;

    @Override
    public void sendVerificationEmail(String to, String username, String token) throws MessagingException {
        String verificationLink = UriComponentsBuilder
            .fromUriString(verificationBaseUrl)
            .path(ApiPaths.USERS + "/verify")
            .queryParam("token", token)
            .toUriString();
        String emailSubject = "Verify your account";
        String emailContent = """
            <!DOCTYPE html>
            <html>
              <body>
                <p>Hi %s,</p>
                <p>Thank you for using our library system.</p>
                <p>Please click the link below to verify your account:</p>
                <p>
                  <a href="%s">%s</a>
                </p>
                <p>If you did not request this, you can ignore this email.</p>
                <p>Best regards,<br>The Library Team</p>
              </body>
            </html>
            """.formatted(username, verificationLink, verificationLink);

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setTo(to);
        helper.setSubject(emailSubject);
        helper.setText(emailContent, true);

        mailSender.send(message);
    }

    @Override
    public void sendBorrowRequestApprovedEmail(
        String to,
        String username,
        String borrowRequestId,
        String startDate,
        String endDate
    ) throws MessagingException {
        String emailSubject = "Your borrow request has been approved!";
        String emailContent = """
            <!DOCTYPE html>
            <html>
              <body>
                <p>Hi %s,</p>
                <p>We’re happy to inform you that your borrow request has been <strong>approved</strong>.</p>
                <p><strong>Request ID:</strong> %s</p>
                <p><strong>Start Date:</strong> %s</p>
                <p><strong>End Date:</strong> %s</p>
                <p>Thank you for using our library system.</p>
                <p>Best regards,<br>The Library Team</p>
              </body>
            </html>
            """.formatted(username, borrowRequestId, startDate, endDate);

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setTo(to);
        helper.setSubject(emailSubject);
        helper.setText(emailContent, true);

        mailSender.send(message);
    }

    @Override
    public void sendBorrowRequestRejectedEmail(
        String to,
        String username,
        String borrowRequestId,
        String reason
    ) throws MessagingException {
        String emailSubject = "Your borrow request has been rejected!";
        String emailContent = """
            <!DOCTYPE html>
            <html>
              <body>
                <p>Hi %s,</p>
                <p>We regret to inform you that your borrow request <strong>%s</strong> has been <strong>rejected</strong>.</p>
                <p><strong>Reason:</strong> %s</p>
                <p>If you have any questions or believe this was a mistake, please feel free to contact the library staff.</p>
                <p>Thank you for understanding.</p>
                <p>Best regards,<br>The Library Team</p>
              </body>
            </html>
            """.formatted(username, borrowRequestId, reason);

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setTo(to);
        helper.setSubject(emailSubject);
        helper.setText(emailContent, true);

        mailSender.send(message);
    }
}
