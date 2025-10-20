package com.book.library.dto;

import com.book.library.model.Book;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BookMapper {

    BookMapper INSTANCE = Mappers.getMapper(BookMapper.class);

    // Entity -> DTO
    BookResponseDTO toDto(Book book);

    // DTO -> Entity  
    Book toEntity(BookRequestDTO bookRequestDTO);

    // List<Entity> -> List<DTO>
    List<BookResponseDTO> toDtoList(List<Book> books);

    void updateEntityFromDto(BookRequestDTO dto, @MappingTarget Book entity);
}