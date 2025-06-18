package com.polarbookshop.orderservice.entity;

import com.polarbookshop.orderservice.domain.OrderStatus;
import java.time.Instant;
import javax.swing.Spring;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "orders")
public record OrderEntity(
        @Id
        Long id,

        String bookIsbn,
        String bookName,
        Double bookPrice,
        Integer quantity,
        OrderStatus status,

        @CreatedDate
        Instant createdDate,

        @LastModifiedDate
        Instant lastModifiedDate,

        @Version
        Long version

) {

    public static OrderEntity of(
            String bookIsbn, String bookName, Double bookPrice, Integer quantity, OrderStatus status
    ) {
        return new OrderEntity(
                null, bookIsbn, bookName, bookPrice, quantity, status, null, null, null
        );
    }
}
