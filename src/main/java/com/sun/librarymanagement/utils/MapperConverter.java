package com.sun.librarymanagement.utils;

import com.sun.librarymanagement.domain.entity.BookEntity;
import org.modelmapper.Converter;

public class MapperConverter {

    public static Converter<BookEntity, Boolean> isFavoritedConverter(BookEntity bookEntity, Long userId) {
        return ctx -> bookEntity.getFavorites().stream()
            .anyMatch(fav -> fav.getId().equals(userId));
    }
}
