package org.booklibrary.api;

import org.booklibrary.entity.PersistedBook;
import org.booklibrary.mapper.PersistedBookMapper;
import org.booklibrary.model.Book;
import org.booklibrary.model.Borrow;
import org.booklibrary.repository.BookRepositoryInterface;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BookApiControllerTest
{
    @Test
    void addOrder(){
        Borrow b = new Borrow();
        b.setBookId(1L);
        b.setStatus(Borrow.StatusEnum.PLACED);

        //orders.save(b);
    }
    @Test
    void addBook()  {
        BookRepositoryInterface repository = new TestBookRepo();
        BookApiDelegate delegate = new BookApiDelegateImpl(repository, null);
        org.booklibrary.model.Book book = new org.booklibrary.model.Book();
        book.setIsbn("9784139697805");
        book.setTitle("The Hobbit");
        book.setAuthor("<NAME>");
        book.setStatus(Book.StatusEnum.AVAILABLE);
        book.setAuthor("JRR Tolkien");

        PersistedBook pb= PersistedBookMapper.toEntity(book);
        delegate.addBook(book);
    }

    @Test
    void deleteBook() {
    }

    @Test
    void findBooksByAuthors() {
    }

    @Test
    void findBooksByStatus() {
    }

    @Test
    void getBookById() {
    }

    @Test
    void updateBook() {
    }

    @Test
    void updateBookWithForm() {
    }

    @Test
    void uploadFile() {
    }
}