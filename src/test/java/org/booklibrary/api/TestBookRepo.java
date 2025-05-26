package org.booklibrary.api;

import org.booklibrary.entity.PersistedBook;
import org.booklibrary.repository.BookRepositoryInterface;
import org.booklibrary.repository.HashMapRepository;
import org.springframework.stereotype.Repository;

@Repository
public class TestBookRepo extends HashMapRepository<PersistedBook, Long> implements BookRepositoryInterface{
    private Long sequenceId = 1L;

    protected TestBookRepo() {
    }

    protected Long getEntityId(PersistedBook book){
        return book.getId();
    };
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
