package com.sun.librarymanagement.domain.service;

import jakarta.mail.MessagingException;

public interface MailService {

    void sendVerificationEmail(
        final String to,
        final String username,
        final String token
    ) throws MessagingException;

    void sendBorrowRequestApprovedEmail(
        final String to,
        final String username,
        final String borrowRequestId,
        final String startDate,
        final String endDate
    ) throws MessagingException;

    void sendBorrowRequestRejectedEmail(
        final String to,
        final String username,
        final String borrowRequestId,
        final String reason
    ) throws MessagingException;
}
