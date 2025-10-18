package com.book.library.controller;

import com.book.library.model.Book;
import com.book.library.repository.BookRepository;
import com.book.library.service.BookService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    public List<Book> getAllBooks() {
        return bookService.getAllBooks();
    }

    @PostMapping
    public Book addBook(@Valid @RequestBody Book book) {
        return bookService.saveBook(book);
    }


    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {
        Optional<Book> book = bookService.getBookById(id);
        return book.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable Long id, @Valid @RequestBody Book updatedBook) {
        Optional<Book> existingBookOpt = bookService.getBookById(id);

        if (existingBookOpt.isPresent()) {
            Book existingBook = existingBookOpt.get();

            existingBook.setTitle(updatedBook.getTitle());
            existingBook.setAuthor(updatedBook.getAuthor());
            existingBook.setPublicationYear(updatedBook.getPublicationYear());
            existingBook.setIsbn(updatedBook.getIsbn());
            existingBook.setAvailable(updatedBook.isAvailable());

            Book savedBook = bookService.saveBook(existingBook);
            return ResponseEntity.ok(savedBook);
        } else {
            return ResponseEntity.notFound().build();
        }
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
    public ResponseEntity<Book> updateBookPartially(@PathVariable Long id,
                                                    @RequestBody Map<String, Object> updates) {
        Optional<Book> existingBookOpt = bookService.getBookById(id);
        //todo validation
        if (existingBookOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Book existingBook = existingBookOpt.get();

        try {
            updates.forEach((key, value) -> {
                switch (key) {
                    case "title" -> existingBook.setTitle(value.toString());
                    case "author" -> existingBook.setAuthor(value.toString());
                    case "publicationYear" -> {
                        if (value instanceof Number) {
                            existingBook.setPublicationYear(((Number) value).intValue());
                        }
                    }
                    case "isbn" -> existingBook.setIsbn(value.toString());
                    case "available" -> {
                        if (value instanceof Boolean) {
                            existingBook.setAvailable((Boolean) value);
                        }
                    }
                    default -> throw new IllegalArgumentException("Invalid field: " + key);
                }
            });

            Book savedBook = bookService.saveBook(existingBook);
            return ResponseEntity.ok(savedBook);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}