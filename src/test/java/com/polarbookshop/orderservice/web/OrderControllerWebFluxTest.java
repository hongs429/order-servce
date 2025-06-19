package com.polarbookshop.orderservice.web;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import com.polarbookshop.orderservice.domain.Order;
import com.polarbookshop.orderservice.domain.OrderStatus;
import com.polarbookshop.orderservice.entity.OrderEntity;
import com.polarbookshop.orderservice.service.OrderService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

@WebFluxTest(OrderController.class) //orderController 를 대상으로 한 스프링 웹플럭스 컴포넌트에 집중하는 테스트 클래스
class OrderControllerWebFluxTest {

    @Autowired
    WebTestClient webTestClient; // 웹 클라이언트의 변형으로 Restful 서비스 테스트를 쉽게 하기 위한 기능을 추가로 가지고 있다.

    @MockitoBean // OrderService의 모의 객체를 스프링 애플리케이션 컨텍스트에 추가
    OrderService orderService;

    @Test
    void whenBookNotAvailableThenRejectedOrder() {
        OrderRequest request = new OrderRequest("1231231231", 3);
        OrderEntity expectedOrder = OrderService.buildRejectedOrder(request.isbn(), request.quantity());

        when(orderService.submitOrder(request.isbn(), request.quantity())).thenReturn(
                Mono.just(Order.of(expectedOrder)));

        webTestClient
                .post()
                .uri("/orders")
                .bodyValue(request)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(Order.class).value(actualOrder -> {
                    assertThat(actualOrder).isNotNull();
                    assertThat(actualOrder.status()).isEqualTo(OrderStatus.REJECTED);
                });


    }


}