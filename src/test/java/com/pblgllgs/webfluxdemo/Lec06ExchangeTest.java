package com.pblgllgs.webfluxdemo;
/*
 *
 * @author pblgl
 * Created on 07-12-2023
 *
 */

import com.pblgllgs.webfluxdemo.dto.InputFailedValidationResponse;
import com.pblgllgs.webfluxdemo.dto.Response;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

class Lec06ExchangeTest extends WebfluxDemoApplicationTests {

    @Autowired
    private WebClient webClient;

    @Test
    void badRequestTest() {

        Mono<Object> responseMono = webClient
                .get()
                .uri("reactive-math-valid/square/{input}/throw", 5)
                .exchangeToMono(this::exchange)
                .doOnNext(System.out::println)
                .doOnError(err -> System.out.println(err.getMessage()));


        StepVerifier.create(responseMono)
                .expectNextCount(1)
                .verifyComplete();

    }

    private Mono<Object> exchange(ClientResponse client) {
        if (client.statusCode().value() == 400) {
            return client.bodyToMono(InputFailedValidationResponse.class);
        } else {
            return client.bodyToMono(Response.class);
        }
    }
}
