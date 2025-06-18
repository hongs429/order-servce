package com.polarbookshop.orderservice.web;


import com.polarbookshop.orderservice.domain.Order;
import com.polarbookshop.orderservice.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("orders")
public class OrderController {

    private final OrderService orderService;

    @GetMapping
    public Flux<Order> getOrders() {
        return orderService.getAllOrders();
    }

    @PostMapping
    public Mono<Order> createOrder(@Valid @RequestBody OrderRequest request) {
        return orderService.submitOrder(request.isbn(), request.quantity());
    }
}
