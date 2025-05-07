package com.sun.librarymanagement.exception;

import jakarta.mail.MessagingException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class AppExceptionHandler {

    @ExceptionHandler(AppException.class)
    public ResponseEntity<AppErrorMessages> handleAppException(AppException exception) {
        return responseErrorMessages(List.of(exception.getMessage()), exception.getAppError().getStatus());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<AppErrorMessages> handleValidationError(MethodArgumentNotValidException exception) {
        List<String> messages = exception.getBindingResult().getFieldErrors().stream().map(this::createFieldErrorMessage).collect(Collectors.toList());
        return responseErrorMessages(messages, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(MessagingException.class)
    public ResponseEntity<AppErrorMessages> handleMessagingException(MessagingException exception) {
        System.out.println(exception.toString());
        return responseErrorMessages(
            List.of("Failed to send email. Please try again later."),
            HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<AppErrorMessages> handleException(Exception exception) {
        return responseErrorMessages(List.of("internal server error"), HttpStatus.UNPROCESSABLE_ENTITY);
    }

    private ResponseEntity<AppErrorMessages> responseErrorMessages(List<String> messages, HttpStatus status) {
        AppErrorMessages errorMessages = new AppErrorMessages();
        messages.forEach(errorMessages::append);
        return new ResponseEntity<>(errorMessages, status);
    }

    private String createFieldErrorMessage(FieldError fieldError) {
        return "[" +
            fieldError.getField() +
            "] must be " +
            fieldError.getDefaultMessage() +
            ". your input: [" +
            fieldError.getRejectedValue() +
            "]";
    }
}
