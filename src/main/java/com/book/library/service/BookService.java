package com.book.library.service;

import com.book.library.dto.BookRequestDTO;
import com.book.library.dto.BookResponseDTO;
import com.book.library.model.Book;
import com.book.library.repository.BookRepository;
import org.hibernate.annotations.NotFound;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {
    private final BookRepository bookRepository;

    @Autowired
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public Optional<Book> getBookById(Long id) {
        return bookRepository.findById(id);
    }

    public Book saveBook(Book book) {
        return bookRepository.save(book);
    }

    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }

    public Book patchBook(Long id, BookRequestDTO patchDTO) {
        Book book = getBookById(id).orElseThrow(() -> new RuntimeException("Book not found"));


        if (patchDTO.getTitle() != null) {
            book.setTitle(patchDTO.getTitle());
        }
        if (patchDTO.getAuthor() != null) {
            book.setAuthor(patchDTO.getAuthor());
        }
        if (patchDTO.getPublicationYear() != null) {
            book.setPublicationYear(patchDTO.getPublicationYear());
        }
        if (patchDTO.getIsbn() != null) {
            book.setIsbn(patchDTO.getIsbn());
        }
        return saveBook(book);
    }

    public boolean bookExists(Long id) {
        return bookRepository.existsById(id);
    }
}
