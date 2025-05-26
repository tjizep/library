package org.booklibrary.api;

import org.booklibrary.configuration.HomeController;
import org.booklibrary.entity.PersistedBook;
import org.booklibrary.mapper.PersistedBookMapper;
import org.booklibrary.model.Book;
import org.booklibrary.model.Borrow;
import org.booklibrary.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import javax.persistence.EntityManagerFactory;
import javax.swing.text.html.parser.Entity;

//@WebMvcTest(HomeController.class)
class BookApiControllerTest {
    //@Autowired
    //private MockMvc mockMvc;

    @Test
    void getDelegate() {
    }
    @Test
    void addOrder(){
        Borrow b = new Borrow();
        b.setBookId(1L);
        b.setStatus(Borrow.StatusEnum.PLACED);

        //orders.save(b);
    }
    @Test
    void addBook() throws Exception {
        org.booklibrary.model.Book book = new org.booklibrary.model.Book();
        book.setIsbn("9784139697805");
        book.setTitle("The Hobbit");
        book.setAuthor("<NAME>");
        book.setStatus(Book.StatusEnum.AVAILABLE);
        book.setAuthor("JRR Tolkien");

        PersistedBook pb= PersistedBookMapper.toEntity(book);
        //mockMvc.perform(get("/v1/book/")).andDo(print()).andExpect(status().isOk());
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