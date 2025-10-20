package com.book.library.controller;

import com.book.library.dto.BookMapper;
import com.book.library.dto.BookRequestDTO;
import com.book.library.dto.BookResponseDTO;
import com.book.library.model.Book;
import com.book.library.service.BookService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;
    private final BookMapper bookMapper;

    public BookController(BookService bookService, BookMapper bookMapper) {
        this.bookService = bookService;
        this.bookMapper = bookMapper;
    }

    @GetMapping
    public List<BookResponseDTO> getAllBooks() {
        List<Book> books = bookService.getAllBooks();
        return bookMapper.toDtoList(books);
    }

    @PostMapping
    public BookResponseDTO addBook(@Valid @RequestBody BookRequestDTO bookRequestDTO) {
        Book entity = bookMapper.toEntity(bookRequestDTO);
        Book savedEntity = bookService.saveBook(entity);
        return bookMapper.toDto(savedEntity);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookResponseDTO> getBookById(@PathVariable Long id) {
        Optional<Book> bookOptional = bookService.getBookById(id);
        if (bookOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Book getBook = bookOptional.get();
        BookResponseDTO bookResponseDTO = bookMapper.toDto(getBook);

        return ResponseEntity.ok(bookResponseDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookResponseDTO> updateBook(@PathVariable Long id, @Valid @RequestBody BookRequestDTO updatedBook) {
        Optional<Book> oldBook = bookService.getBookById(id);
        Book bookId = oldBook.get();
        bookMapper.updateEntityFromDto(updatedBook, bookId);
        Book savedBook = bookService.saveBook(bookId);
        BookResponseDTO bookResponseDTO = bookMapper.toDto(savedBook);
        return ResponseEntity.ok(bookResponseDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBookById(@PathVariable Long id) {
        if (bookService.bookExists(id)) {
            bookService.deleteBook(id);
            return ResponseEntity.ok("Book is deleted");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<BookResponseDTO> updateBookPartially(@PathVariable Long id,
                                                    @RequestBody BookRequestDTO updates) {
        Book updatedBook = bookService.patchBook(id,updates);
        BookResponseDTO bookResponseDTO = bookMapper.toDto(updatedBook);
        return ResponseEntity.ok(bookResponseDTO);
    }
}