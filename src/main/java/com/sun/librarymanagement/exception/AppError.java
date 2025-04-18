package com.sun.librarymanagement.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AppError {
    LOGIN_INFO_INVALID("Invalid email or password.", HttpStatus.UNAUTHORIZED),
    ACCOUNT_ALREADY_EXISTS("Account already exists.", HttpStatus.CONFLICT),
    ACCOUNT_NOT_ACTIVE("Account is deactivated.", HttpStatus.FORBIDDEN),
    ACCOUNT_NOT_VERIFIED("Account is not verified.", HttpStatus.FORBIDDEN),
    VERIFICATION_TOKEN_INVALID("Verification token is invalid or expired.", HttpStatus.FORBIDDEN),
    USER_ALREADY_VERIFIED("User is already verified.", HttpStatus.BAD_REQUEST),
    USER_NOT_FOUND("User not found.", HttpStatus.NOT_FOUND),
    PUBLISHER_NOT_FOUND("Publisher not found", HttpStatus.NOT_FOUND),
    PUBLISHER_ALREADY_EXISTS("Publisher already exists.", HttpStatus.CONFLICT),
    ;

    private final String message;

    private final HttpStatus status;
}
