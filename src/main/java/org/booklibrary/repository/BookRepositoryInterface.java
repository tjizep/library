package org.booklibrary.repository;

import org.booklibrary.entity.PersistedBook;
import org.booklibrary.model.Book;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BookRepositoryInterface extends CrudRepository<PersistedBook, Long> {
    @Override
    <S extends PersistedBook> S save(S book);

    @Override
    void deleteAllById(Iterable<? extends Long> longs);

}
