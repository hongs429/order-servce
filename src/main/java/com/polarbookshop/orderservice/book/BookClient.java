package com.polarbookshop.orderservice.book;

import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

@Component
@RequiredArgsConstructor
public class BookClient {

    private static final String BOOKS_ROOT_API = "/books/";
    private final WebClient webClient;


    public Mono<Book> getBookByIsbn(String isbn) {
        return webClient
                .get()
                .uri(BOOKS_ROOT_API + isbn)
                .retrieve()
                .bodyToMono(Book.class)
                .timeout(Duration.ofSeconds(3), Mono.empty())  // 3초안에 응답이 와야 한다.
                .onErrorResume(WebClientResponseException.NotFound.class, // 404에 대한 예외는 그냥 monoEmpty로 처리하겟다
                        exception -> Mono.empty())
                .retryWhen(
                        Retry.backoff(3, Duration.ofMillis(100)) // 지수 백오프를 재시도 전략
                )
                .onErrorResume(Exception.class, ex -> Mono.empty());

    }
}
