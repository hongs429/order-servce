package com.polarbookshop.orderservice.domain;

import com.polarbookshop.orderservice.entity.OrderEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface OrderRepository extends ReactiveCrudRepository<OrderEntity, Long> {
}
