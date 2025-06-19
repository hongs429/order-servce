package com.polarbookshop.orderservice.domain;

import com.polarbookshop.orderservice.config.DataConfig;
import com.polarbookshop.orderservice.entity.OrderEntity;
import com.polarbookshop.orderservice.service.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import reactor.test.StepVerifier;

@DataR2dbcTest
@Import(DataConfig.class)
@Testcontainers
class OrderRepositoryR2dbcTest {

    @Container
    static PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer("postgres:14.4");

    @Autowired
    OrderRepository orderRepository;

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.r2dbc.url", OrderRepositoryR2dbcTest::r2dbcUrl);
        registry.add("spring.r2dbc.username", postgreSQLContainer::getUsername);
        registry.add("spring.r2dbc.password", postgreSQLContainer::getPassword);
        registry.add("spring.flyway.url", postgreSQLContainer::getJdbcUrl);
    }

    private static String r2dbcUrl() { // 테스트컨테이너가 JDBC와는 다르게 R2DBC에 대해서는 연결 문자영르 제공하지 않기 때문에 연결 문자열 생성
        return String.format("r2dbc:postgresql://%s:%s/%s",
                postgreSQLContainer.getHost(),
                postgreSQLContainer.getMappedPort(PostgreSQLContainer.POSTGRESQL_PORT),
                postgreSQLContainer.getDatabaseName()
        );

    }

    @Test
    void createRejectedOrder() {
        OrderEntity rejectedOrder = OrderService.buildRejectedOrder("1231231231", 3);

        StepVerifier
                .create(orderRepository.save(rejectedOrder)) // StepVerifier 객체를 OrderRepository가 반환하는 객체로 초기화
                .expectNextMatches( // 반환된 주문이 올바른 상태를 가지고 있는지 확인
                        order -> order.status().equals(OrderStatus.REJECTED)
                )
                .verifyComplete(); // 리엑티브 스트림이 성공적으로 완료됐는지 확인
    }



}