package org.booklibrary.api;

import org.booklibrary.mapper.PersistedBookMapper;
import org.booklibrary.model.Book;
import org.booklibrary.model.Borrow;
import org.booklibrary.repository.OrderRepository;
import org.booklibrary.repository.BookRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.server.ResponseStatusException;

import javax.annotation.PostConstruct;
import java.time.OffsetDateTime;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class LibraryApiDelegateImpl implements LibraryApi {

    private final OrderRepository orderRepository;

    private final BookRepository bookRepository;

    private final NativeWebRequest request;

    public LibraryApiDelegateImpl(OrderRepository orderRepository, BookRepository bookRepository, NativeWebRequest request) {
        this.orderRepository = orderRepository;
        this.bookRepository = bookRepository;
        this.request = request;
    }

    @PostConstruct
    void initOrders() {
        orderRepository.save(createBorrow(1, 1, Borrow.StatusEnum.PLACED));
        orderRepository.save(createBorrow(2, 1, Borrow.StatusEnum.DELIVERED));
        orderRepository.save(createBorrow(3, 2, Borrow.StatusEnum.PLACED));
        orderRepository.save(createBorrow(4, 2, Borrow.StatusEnum.DELIVERED));
        orderRepository.save(createBorrow(5, 3, Borrow.StatusEnum.PLACED));
        orderRepository.save(createBorrow(6, 3, Borrow.StatusEnum.PLACED));
        orderRepository.save(createBorrow(7, 3, Borrow.StatusEnum.PLACED));
        orderRepository.save(createBorrow(8, 3, Borrow.StatusEnum.PLACED));
        orderRepository.save(createBorrow(9, 3, Borrow.StatusEnum.PLACED));
        orderRepository.save(createBorrow(10, 3, Borrow.StatusEnum.PLACED));
    }


    @Override
    public ResponseEntity<Void> deleteBorrow(String orderId) {
        Borrow borrow = orderRepository.findById(Long.valueOf(orderId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        orderRepository.delete(borrow);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Map<String, Integer>> getInventory() {
        ApiUtil.checkApiKey(request);
        return ResponseEntity.ok(bookRepository.findAll().stream()
                .map(PersistedBookMapper::toDto)
                .map(Book::getStatus)
                .collect(Collectors.groupingBy(Book.StatusEnum::toString, Collectors.reducing(0, e -> 1, Integer::sum))));
    }

    @Override
    public ResponseEntity<Borrow> getBorrowById(Long orderId) {
        return orderRepository.findById(orderId)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found"));
    }

    @Override
    public ResponseEntity<Borrow> placeBook(Borrow borrow) {
        return ResponseEntity.ok(orderRepository.save(borrow));
    }

    private static Borrow createBorrow(long id, long bookId, Borrow.StatusEnum status) {
        return new Borrow()
                .id(id)
                .bookId(bookId)
                .quantity(2)
                .borrowDate(OffsetDateTime.now())
                .status(status);
    }
}
