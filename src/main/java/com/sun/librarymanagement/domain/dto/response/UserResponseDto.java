package com.sun.librarymanagement.domain.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Builder
public class UserResponseDto {

    private String token;

    private String username;

    private String email;

    private Boolean isActive;

    private Boolean isVerified;
}
