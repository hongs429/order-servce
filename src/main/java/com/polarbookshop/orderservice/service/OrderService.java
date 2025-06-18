package com.polarbookshop.orderservice.service;


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


    public Flux<Order> getAllOrders() {
        Flux<OrderEntity> result = orderRepository.findAll();
        return result.map(Order::of);
    }

    public Mono<Order> submitOrder(String isbn, int quantity) {
        return Mono.just(buildRejectedOrder(isbn, quantity))
                .flatMap(orderRepository::save)
                .map(Order::of);
    }

    public static OrderEntity buildRejectedOrder(String isbn, int quantity) {
        return OrderEntity.of(isbn, null, null, quantity, OrderStatus.REJECTED);
    }


}
