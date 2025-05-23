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
    USERNAME_ALREADY_EXISTS("Username already exists.", HttpStatus.CONFLICT),
    EMAIL_ALREADY_EXISTS("Email already exists.", HttpStatus.CONFLICT),
    OLD_PASSWORD_INVALID("Invalid old password.", HttpStatus.BAD_REQUEST),
    PASSWORDS_DO_NOT_MATCH("New password and confirm password do not match.", HttpStatus.BAD_REQUEST),
    PUBLISHER_NOT_FOUND("Publisher not found", HttpStatus.NOT_FOUND),
    PUBLISHER_ALREADY_EXISTS("Publisher already exists.", HttpStatus.CONFLICT),
    AUTHOR_NOT_FOUND("Author not found", HttpStatus.NOT_FOUND),
    AUTHOR_ALREADY_EXISTS("Author already exists.", HttpStatus.CONFLICT),
    PUBLISHER_ALREADY_FOLLOWED("Publisher already followed.", HttpStatus.CONFLICT),
    PUBLISHER_NOT_FOLLOWED("Publisher is not followed.", HttpStatus.NOT_FOUND),
    AUTHOR_ALREADY_FOLLOWED("Author already followed.", HttpStatus.CONFLICT),
    AUTHOR_NOT_FOLLOWED("Author is not followed.", HttpStatus.NOT_FOUND),
    CATEGORY_ALREADY_EXISTS("Category already exists.", HttpStatus.CONFLICT),
    CATEGORY_NOT_FOUND("Category not found", HttpStatus.NOT_FOUND),
    BOOK_NOT_FOUND("Book not found", HttpStatus.NOT_FOUND),
    FAVORITE_NOT_FOUND("Favorite not found.", HttpStatus.NOT_FOUND),
    BOOK_ALREADY_FAVORITED("Favorite already favorited.", HttpStatus.CONFLICT),
    BORROW_REQUEST_NOT_FOUND("Borrow request not found.", HttpStatus.NOT_FOUND),
    BORROW_REQUEST_INVALID("Borrow request is invalid.", HttpStatus.BAD_REQUEST),
    BORROW_REQUEST_NOT_APPROVED("Borrow request is not approved.", HttpStatus.BAD_REQUEST),
    BORROW_REQUEST_CANNOT_BE_APPROVED("Borrow request cannot be approved.", HttpStatus.BAD_REQUEST),
    BORROW_REQUEST_CANNOT_BE_REJECTED("Borrow request cannot be rejected.", HttpStatus.BAD_REQUEST),
    BORROW_REQUEST_CANNOT_BE_REJECTED_WITHOUT_A_REASON("Borrow request cannot be rejected without a reason.", HttpStatus.BAD_REQUEST),
    BORROW_REQUEST_CANNOT_BE_CANCELLED("Borrow request cannot be cancelled.", HttpStatus.BAD_REQUEST),
    BORROW_REQUEST_CANNOT_BE_COMPLETED("Borrow request cannot be completed.", HttpStatus.BAD_REQUEST),
    COMMENT_NOT_FOUND("Comment not found", HttpStatus.NOT_FOUND),
    COMMENT_NOT_ASSOCIATED_WITH_BOOK("Comment is not associated with this book.", HttpStatus.BAD_REQUEST),
    COMMENT_NOT_ASSOCIATED_WITH_USER("Comment is not associated with this user.", HttpStatus.BAD_REQUEST),
    EXCEL_EXPORT_FAILED("Failed to export Excel file", HttpStatus.INTERNAL_SERVER_ERROR)
    ;

    private final String message;

    private final HttpStatus status;
}
