package com.sun.librarymanagement.domain.controller.admin;

import com.sun.librarymanagement.domain.dto.request.BookRequestDto;
import com.sun.librarymanagement.domain.dto.request.SearchBookRequestDto;
import com.sun.librarymanagement.domain.dto.response.BookResponseDto;
import com.sun.librarymanagement.domain.entity.BookEntity;
import com.sun.librarymanagement.domain.export.BookExcelExporter;
import com.sun.librarymanagement.domain.service.BookService;
import com.sun.librarymanagement.utils.ApiPaths;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

import static com.sun.librarymanagement.utils.Constant.BOOK_CSV_FILE_NAME;

@RestController("adminBookController")
@RequestMapping(ApiPaths.BOOKS_ADMIN)
@AllArgsConstructor()
public class BookController extends AdminController {

    private final BookService bookService;

    @PostMapping
    public ResponseEntity<BookResponseDto> addBook(@RequestBody @Valid BookRequestDto request) {
        BookResponseDto response = bookService.addBook(request);
        URI location = URI.create(ApiPaths.BOOKS + "/" + response.getId());
        return ResponseEntity.created(location).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookResponseDto> updateBook(@PathVariable long id, @RequestBody @Valid BookRequestDto request) {
        BookResponseDto response = bookService.updateBook(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable long id) {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/export")
    public ResponseEntity<byte[]> exportToExcel(@Valid @RequestBody SearchBookRequestDto request) {
        List<BookEntity> books = bookService.search(request);
        BookExcelExporter exporter = new BookExcelExporter();
        byte[] excelData = exporter.exportToByteArray(books);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_PLAIN);
        headers.setContentDisposition(
            ContentDisposition
                .attachment()
                .filename(BOOK_CSV_FILE_NAME)
                .build()
        );
        return ResponseEntity
            .ok()
            .headers(headers)
            .body(excelData);
    }
}
