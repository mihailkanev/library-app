package com.book.library.controller;

import com.book.library.model.Book;
import com.book.library.repository.BookRepository;
import com.book.library.service.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.catalina.Service;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private BookService bookService;
    private BookController bookController;

    private ObjectMapper objectMapper;
    private Book testBook1;
    private Book testBook2;

    @BeforeEach
    void setUp() {
        testBook1 = new Book("Book1", "Steven C", 2222, "ASDWASD");
        testBook2 = new Book("Book2", "Steven A", 333, "IBSNF");
        bookController = new BookController(bookService);
        mockMvc = MockMvcBuilders.standaloneSetup(bookController).build();
    }

    @Test
    void returnAllBooks() throws Exception {
        when(bookService.getAllBooks()).thenReturn(Arrays.asList(testBook1, testBook2));
        mockMvc.perform(get("/api/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].title").value("Book1"));
    }
    @Test
    void returnBookThatExistById() throws Exception {
        testBook1.setId(1L);
        when(bookService.getBookById(1L)).thenReturn(Optional.of(testBook1));
        mockMvc.perform(get("/api/books/{id}", 1L))
                .andExpect(status().isOk());
    }
    @Test
    void returnBookThatNotExistById() throws Exception {
        testBook1.setId(99L);
        when(bookService.getBookById(99L)).thenReturn(Optional.of(testBook1));
        mockMvc.perform(get("/api/books/{id}", 99L))
                .andExpect(status().isOk());
    }
}