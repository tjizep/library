
package org.booklibrary.mapper;

import org.booklibrary.entity.PersistedBook;
import org.booklibrary.model.Book;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Converts between the DTO {@link Book} and the JPA entity {@link PersistedBook}.
 */
public final class PersistedBookMapper {

    private PersistedBookMapper() {
        /* static-only class */
    }

    public static PersistedBook toEntity(Book dto) {
        if (dto == null) {
            return null;
        }
        PersistedBook entity = new PersistedBook(
                dto.getTitle(),
                dto.getAuthor(),
                LocalDate.now(),
                dto.getIsbn(),
                dto.getStatus().toString(),
                new BigDecimal(0));
        entity.setId(dto.getId());
        return entity;
    }

    public static Book toDto(PersistedBook entity) {
        if (entity == null) {
            return null;
        }
        return new Book()
                .id(entity.getId())
                .title(entity.getTitle())
                .author(entity.getAuthor())
                .isbn(entity.getIsbn());
    }

    public static List<PersistedBook> toEntityList(Collection<Book> dtos) {
        return dtos == null
                ? List.of()
                : dtos.stream()
                .map(PersistedBookMapper::toEntity)
                .collect(Collectors.toList());
    }

    public static List<Book> toDtoList(Collection<PersistedBook> entities) {
        return entities == null
                ? List.of()
                : entities.stream()
                .map(PersistedBookMapper::toDto)
                .collect(Collectors.toList());
    }
}
