package com.book.library.controller;

import com.book.library.dto.BookMapper;
import com.book.library.dto.BookRequestDTO;
import com.book.library.dto.BookResponseDTO;
import com.book.library.model.Book;
import com.book.library.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// ✅ ПРЕМАХНИ SpringBootTest и използвай само MockitoExtension
@ExtendWith(MockitoExtension.class)
class BookControllerTest {

    @InjectMocks
    private BookController bookController;

    @Mock
    private BookService bookService;

    @Mock
    private BookMapper bookMapper;
    private MockMvc mockMvc;
    private Book testBook1;
    private Book testBook2;
    private BookResponseDTO responseDTO1;
    private BookResponseDTO responseDTO2;
    private BookRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(bookController).build();

        testBook1 = new Book("Book1", "Author1", 1988, "ISBN123");
        testBook2 = new Book("Book2", "Author2", 1999, "ISBN456");
        testBook1.setId(1L);
        testBook2.setId(2L);

        responseDTO1 = new BookResponseDTO(1L, "Book1", "Author1", 1988, "ISBN123");
        responseDTO2 = new BookResponseDTO(2L, "Book2", "Author2", 1999, "ISBN456");
        requestDTO = new BookRequestDTO("Book1", "Author1", 1988, "ISBN123");
    }

    @Test
    void returnAllBooks() throws Exception {
        when(bookService.getAllBooks()).thenReturn(Arrays.asList(testBook1, testBook2));

        when(bookMapper.toDtoList(Arrays.asList(testBook1, testBook2)))
                .thenReturn(Arrays.asList(responseDTO1, responseDTO2));

        mockMvc.perform(get("/api/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].title").value("Book1"))
                .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    void returnBookThatExistById() throws Exception {
        when(bookService.getBookById(1L)).thenReturn(Optional.of(testBook1));
        when(bookMapper.toDto(testBook1)).thenReturn(responseDTO1);

        mockMvc.perform(get("/api/books/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Book1"));
    }

    @Test
    void returnBookThatNotExistById() throws Exception {
        when(bookService.getBookById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/books/{id}", 99L))
                .andExpect(status().isNotFound());  // ✅ ПРОМЕНИ на isNotFound()
    }
}