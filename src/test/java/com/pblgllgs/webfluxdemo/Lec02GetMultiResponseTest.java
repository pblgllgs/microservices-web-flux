package com.pblgllgs.webfluxdemo;
/*
 *
 * @author pblgl
 * Created on 07-12-2023
 *
 */

import com.pblgllgs.webfluxdemo.dto.Response;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

class Lec02GetMultiResponseTest extends WebfluxDemoApplicationTests {

    @Autowired
    private WebClient webClient;

    @Test
    void fluxTest() {
        Flux<Response> response = webClient
                .get()
                .uri("reactive-math/table/{input}", 5)
                .retrieve()
                .bodyToFlux(Response.class)
                .doOnNext(System.out::println);

        StepVerifier.create(response)
                .expectNextCount(10)
                .verifyComplete();
    }

    @Test
    void fluxStreamTest() {
        Flux<Response> response = webClient
                .get()
                .uri("reactive-math/table/{input}/stream", 5)
                .retrieve()
                .bodyToFlux(Response.class)
                .doOnNext(System.out::println);

        StepVerifier.create(response)
                .expectNextCount(10)
                .verifyComplete();
    }
}
