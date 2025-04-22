package com.sun.librarymanagement.domain.controller;

import com.sun.librarymanagement.domain.dto.request.AuthenticationRequestDto;
import com.sun.librarymanagement.domain.dto.request.RegistrationRequestDto;
import com.sun.librarymanagement.domain.dto.request.ResendVerificationEmailRequestDto;
import com.sun.librarymanagement.domain.dto.response.SuccessResponseDto;
import com.sun.librarymanagement.domain.dto.response.UserResponseDto;
import com.sun.librarymanagement.domain.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping
    public ResponseEntity<SuccessResponseDto> registration(@RequestBody @Valid RegistrationRequestDto user) {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(authService.registration(user));
    }

    @GetMapping("/verify")
    public ResponseEntity<SuccessResponseDto> verifyEmail(@RequestParam String token) {
        return ResponseEntity.ok(authService.verifyEmail(token));
    }

    @PostMapping("/verify/resend")
    public ResponseEntity<SuccessResponseDto> resendVerificationEmail(
        @RequestBody @Valid ResendVerificationEmailRequestDto verification
    ) {
        return ResponseEntity.ok(authService.resendVerification(verification.getEmail()));
    }

    @PostMapping("/login")
    public ResponseEntity<UserResponseDto> authentication(@RequestBody @Valid AuthenticationRequestDto user) {
        return ResponseEntity.ok(authService.authentication(user));
    }
}
