package com.sun.librarymanagement.domain.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sun.librarymanagement.domain.model.BorrowRequestStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Builder
public class BorrowRequestResponseDto {

    private Long id;

    private UserResponseDto borrower;

    private BorrowRequestStatus status;

    @JsonProperty("status_note")
    private String statusNote;

    @JsonProperty("start_date")
    private LocalDate startDate;

    @JsonProperty("end_date")
    private LocalDate endDate;

    private List<BorrowRequestDetailResponseDto> details;
}
