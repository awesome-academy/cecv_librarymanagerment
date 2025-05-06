package com.sun.librarymanagement.domain.export;

import com.sun.librarymanagement.domain.entity.BookEntity;
import com.sun.librarymanagement.domain.entity.CategoryEntity;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

import static com.sun.librarymanagement.utils.Constant.BOOK_EXPORT_HEADERS;

public class BookExcelExporter {

    public byte[] exportToByteArray(List<BookEntity> books) {
        StringBuilder csvBuilder = new StringBuilder();
        csvBuilder.append(String.join(",", BOOK_EXPORT_HEADERS)).append("\n");
        for (BookEntity book : books) {
            String categories = book.getCategories().stream()
                .map(CategoryEntity::getName)
                .collect(Collectors.joining("|"));
            csvBuilder
                .append(book.getId()).append(",")
                .append(escape(book.getName())).append(",")
                .append(escape(book.getAuthor().getName())).append(",")
                .append(escape(book.getPublisher().getName())).append(",")
                .append(escape(categories)).append(",")
                .append(book.getQuantity()).append(",")
                .append(book.getAvailableQuantity()).append("\n");
        }
        return csvBuilder.toString().getBytes(StandardCharsets.UTF_8);
    }

    private String escape(String value) {
        if (value == null) return "";
        return "\"" + value.replace("\"", "\"\"") + "\"";
    }
}

