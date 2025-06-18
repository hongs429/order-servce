package com.polarbookshop.orderservice.domain;

import com.polarbookshop.orderservice.entity.OrderEntity;
import java.time.Instant;

public record Order(
        Long id,
        String bookIsbn,
        String bookName,
        Double bookPrice,
        Integer quantity,
        OrderStatus status,
        Instant createdDate,
        Instant lastModifiedDate
) {

    public static Order of(
            Long id, String bookIsbn, String bookName, Double bookPrice, Integer quantity,
            OrderStatus status, Instant createdDate, Instant lastModifiedDate) {
        return new Order(
                id,
                bookIsbn,
                bookName,
                bookPrice,
                quantity,
                status,
                createdDate,
                lastModifiedDate
        );
    }

    public static Order of(OrderEntity entity) {
        return new Order(
                entity.id(),
                entity.bookIsbn(),
                entity.bookName(),
                entity.bookPrice(),
                entity.quantity(),
                entity.status(),
                entity.createdDate(),
                entity.lastModifiedDate()
        );
    }
}
