package org.booklibrary.repository;

import org.booklibrary.entity.PersistedBook;
import org.booklibrary.model.Book;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class BookRepository extends JpaEntityRepository<PersistedBook, Long> implements BookRepositoryInterface {

    private Long sequenceId = 1L;

    protected BookRepository() {
        super(PersistedBook.class);
    }

    @Override
    protected <S extends PersistedBook> Long getEntityId(S book) {
        return book.getId();
    }
    private static boolean validateISBN(PersistedBook b){

        return true;
    }
    @Override
    public <S extends PersistedBook> S save(S book) {
        if (book.getId() != null && book.getId() > sequenceId) {
            sequenceId = book.getId() + 1;
        }
        if (book.getId() == null) {
            book.setId(sequenceId);
            sequenceId += 1;
        }

        return super.save(book);
    }

    @Override
    public void deleteAllById(Iterable<? extends Long> longs) {
        longs.forEach(id -> super.deleteById(id));
    }


}
