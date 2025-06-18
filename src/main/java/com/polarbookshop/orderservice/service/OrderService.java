package com.polarbookshop.orderservice.service;


import com.polarbookshop.orderservice.book.Book;
import com.polarbookshop.orderservice.book.BookClient;
import com.polarbookshop.orderservice.domain.Order;
import com.polarbookshop.orderservice.domain.OrderRepository;
import com.polarbookshop.orderservice.domain.OrderStatus;
import com.polarbookshop.orderservice.entity.OrderEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final BookClient bookClient;


    public Flux<Order> getAllOrders() {
        Flux<OrderEntity> result = orderRepository.findAll();
        return result.map(Order::of);
    }

    public Mono<Order> submitOrder(String isbn, int quantity) {
        return bookClient.getBookByIsbn(isbn)
                .map(book -> buildAcceptedOrder(book, quantity))
                .defaultIfEmpty(
                        buildRejectedOrder(isbn, quantity)
                )
                .flatMap(orderRepository::save)
                .map(Order::of);
    }

    public static OrderEntity buildRejectedOrder(String isbn, int quantity) {
        return OrderEntity.of(isbn, null, null, quantity, OrderStatus.REJECTED);
    }

    public static OrderEntity buildAcceptedOrder(Book book, int quantity) {
        return OrderEntity.of(
                book.isbn(),
                book.title() + " - " + book.author(),
                book.price(),
                quantity,
                OrderStatus.ACCEPTED
        );
    }

}
