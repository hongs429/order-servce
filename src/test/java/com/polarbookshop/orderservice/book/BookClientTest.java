package com.polarbookshop.orderservice.book;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

class BookClientTest {

    private MockWebServer mockWebServer;
    private BookClient bookClient;

    @BeforeEach
    void setUp() throws IOException {
        this.mockWebServer = new MockWebServer();
        this.mockWebServer.start(); // 테스트 케이스를 실행하기 앞서 모의 서버를 시작
        var webClient = WebClient.builder()
                .baseUrl(mockWebServer.url("/").toString()) // 모의 서버의 URL을 웹클라이언트의 베이스 url로 사용
                .build();
        this.bookClient = new BookClient(webClient);
    }

    @AfterEach
    void tearDown() throws IOException {
        this.mockWebServer.close(); // 테스트 케이스가 끝나면, 모의 서버 중
    }


    @Test
    void whenBookExistsThenReturnBook() {
        String bookIsbn = "1231231230";

        MockResponse mockResponse = new MockResponse();
        mockResponse.addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        mockResponse.setBody("""
                {
                    "isbn": %s,
                    "title": "Title",
                    "author": "Author",
                    "price": 9.90,
                    "publisher": "Polarsophia"
                }
                """.formatted(bookIsbn));

        mockWebServer.enqueue(mockResponse);

        Mono<Book> book = bookClient.getBookByIsbn(bookIsbn);

        StepVerifier.create(book) // bookClient가 반환하는 객체로 stepVerifier 객체를 초기화한다.
                .expectNextMatches(
                        b -> b.isbn().equals(bookIsbn) // 반환된 책의 isbn이 요청한 isbn과 같은지 확인한다.
                )
                .verifyComplete(); // 리엑티브 스트림이 성공적으로 완료됬는지 확인한다.
    }
}