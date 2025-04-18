package com.sun.librarymanagement.domain.service;

public interface MailService {

    void sendVerificationEmail(final String to, final String token);
}
