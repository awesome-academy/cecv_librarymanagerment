package com.sun.librarymanagement.utils;

import com.sun.librarymanagement.domain.dto.response.BookResponseDto;
import com.sun.librarymanagement.domain.dto.response.BorrowRequestDetailResponseDto;
import com.sun.librarymanagement.domain.dto.response.BorrowRequestResponseDto;
import com.sun.librarymanagement.domain.dto.response.UserResponseDto;
import com.sun.librarymanagement.domain.entity.BookEntity;
import com.sun.librarymanagement.domain.entity.BorrowRequestDetailEntity;
import com.sun.librarymanagement.domain.entity.BorrowRequestEntity;
import com.sun.librarymanagement.domain.entity.UserEntity;
import org.modelmapper.Converter;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

public class MapperConverter {

    public static Converter<BookEntity, Boolean> isFavoritedConverter(BookEntity bookEntity, Long userId) {
        return ctx -> bookEntity.getFavorites().stream()
            .anyMatch(fav -> fav.getId().equals(userId));
    }

    public static BorrowRequestResponseDto convertToBorrowRequestResponseDto(BorrowRequestEntity entity) {
        return BorrowRequestResponseDto.builder()
            .id(entity.getId())
            .borrower(convertToUserResponseDto(entity.getBorrower()))
            .status(entity.getStatus())
            .statusNote(entity.getStatusNote())
            .startDate(entity.getStartDate())
            .endDate(entity.getEndDate())
            .details(convertToBorrowRequestDetailList(entity.getDetailList()))
            .build();
    }

    public static BorrowRequestDetailResponseDto convertToBorrowRequestDetailResponseDto(BorrowRequestDetailEntity entity) {
        return BorrowRequestDetailResponseDto.builder()
            .id(entity.getId())
            .book(convertToBookResponseDto(entity.getBook()))
            .quantity(entity.getQuantity())
            .status(entity.getStatus())
            .borrowDate(entity.getBorrowDate())
            .returnDate(entity.getReturnDate())
            .build();
    }

    public static UserResponseDto convertToUserResponseDto(UserEntity entity) {
        return UserResponseDto.builder()
            .id(entity.getId())
            .username(entity.getUsername())
            .email(entity.getEmail())
            .build();
    }

    public static BookResponseDto convertToBookResponseDto(BookEntity entity) {
        return BookResponseDto.builder()
            .id(entity.getId())
            .name(entity.getName())
            .build();
    }

    public static List<BorrowRequestResponseDto> convertToBorrowRequestList(
        Page<BorrowRequestEntity> borrowRequestEntities
    ) {
        return borrowRequestEntities.stream()
            .map(MapperConverter::convertToBorrowRequestResponseDto)
            .collect(Collectors.toList());
    }

    public static List<BorrowRequestDetailResponseDto> convertToBorrowRequestDetailList(
        List<BorrowRequestDetailEntity> borrowRequestDetailEntities
    ) {
        return borrowRequestDetailEntities.stream()
            .map(MapperConverter::convertToBorrowRequestDetailResponseDto)
            .collect(Collectors.toList());
    }
}
