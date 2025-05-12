package com.sun.librarymanagement.domain.service.impl;

import com.sun.librarymanagement.domain.dto.request.RateBookRequestDto;
import com.sun.librarymanagement.domain.dto.response.BookResponseDto;
import com.sun.librarymanagement.domain.entity.BookEntity;
import com.sun.librarymanagement.domain.entity.RateEntity;
import com.sun.librarymanagement.domain.model.RateInfo;
import com.sun.librarymanagement.domain.repository.BookRepository;
import com.sun.librarymanagement.domain.repository.RateRepository;
import com.sun.librarymanagement.domain.service.RateService;
import com.sun.librarymanagement.exception.AppError;
import com.sun.librarymanagement.exception.AppException;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.sun.librarymanagement.utils.MapperConverter.isFavoritedConverter;

@Service
@RequiredArgsConstructor
public class RateServiceImpl implements RateService {
    private final BookRepository bookRepository;
    private final RateRepository rateRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public BookResponseDto addBookRating(
        @NotNull RateBookRequestDto request,
        long userId
    ) {
        BookEntity book = bookRepository.findByIdWithLock(request.getBookId()).orElseThrow(() -> new AppException(AppError.BOOK_NOT_FOUND));
        RateInfo rateInfo = new RateInfo(userId, request.getBookId());
        rateRepository.save(new RateEntity(rateInfo, request.getRate()));
        return convertToBookResponseDto(book, userId);
    }

    @Override
    @Transactional
    public void deleteBookRating(long userId, long bookId) {
        Optional<RateEntity> rateEntity = rateRepository.findByIdWithLock(userId, bookId);
        if (rateEntity.isEmpty()) {
            throw new AppException(AppError.BOOK_RATING_NOT_FOUND);
        }
        rateRepository.delete(rateEntity.get());
    }

    private BookResponseDto convertToBookResponseDto(BookEntity bookEntity, long currentUserId) {
        BookResponseDto bookResponseDto = modelMapper
            .typeMap(BookEntity.class, BookResponseDto.class)
            .addMappings(
                    mapper -> mapper
                            .using(isFavoritedConverter(bookEntity, currentUserId))
                            .map(src -> src, BookResponseDto::setFavorited)
            )
            .map(bookEntity);
        Optional<RateEntity> rateEntity = rateRepository.findById(currentUserId, bookEntity.getId());
        rateEntity.ifPresent(entity -> bookResponseDto.setRate(entity.getRate()));
        return bookResponseDto;
    }
}
