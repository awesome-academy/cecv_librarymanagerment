package com.sun.librarymanagement.domain.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class ChangePasswordRequestDto {

    @NotBlank(message = "Old password is required.")
    @Size(min = 8, message = "Password must be at least 8 characters.")
    @JsonProperty("old_password")
    private String oldPassword;

    @NotBlank(message = "New password is required.")
    @Size(min = 8, message = "Password must be at least 8 characters.")
    @JsonProperty("new_password")
    private String newPassword;
}
