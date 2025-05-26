
package org.booklibrary.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

/**
 * JPA entity that represents a book in the catalogue.
 */
@Entity
@Table(name = "books")
@Setter
@Getter
public class PersistedBook {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(nullable = false, length = 50)
    private String status;

    @Column(nullable = false, length = 150)
    private String author;

    @Column(name = "published_date")
    private LocalDate publishedDate;

    @Column(unique = true, length = 20)
    private String isbn;

    @Column(precision = 9, scale = 2)
    private BigDecimal price;

    /* ------------------------------------------------------------------------ */
    /*  Constructors                                                            */
    /* ------------------------------------------------------------------------ */

    protected PersistedBook() {
        /* Required by JPA */
    }

    public PersistedBook(String title,
                         String author,
                         LocalDate publishedDate,
                         String isbn,
                         String status,
                         BigDecimal price) {
        this.title = title;
        this.author = author;
        this.publishedDate = publishedDate;
        this.isbn = isbn;
        this.status = status;
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PersistedBook)) return false;
        PersistedBook book = (PersistedBook) o;
        return Objects.equals(id, book.id);
    }

    @Override
    public int hashCode() {
        return (id != null) ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", publishedDate=" + publishedDate +
                ", isbn='" + isbn + '\'' +
                ", price=" + price +
                '}';
    }
}



