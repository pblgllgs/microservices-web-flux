package com.pblgllgs.webfluxdemo;
/*
 *
 * @author pblgl
 * Created on 07-12-2023
 *
 */

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.Map;

class Lec07QueryParamsTest extends WebfluxDemoApplicationTests {

    @Autowired
    private WebClient webClient;

    @Test
    void queryParamsTest() {

        Map<String, Integer> map = Map.of(
                "count", 20, "page", 10
        );

        Flux<Integer> flux = this.webClient
                .get()
                .uri(uriBuilder ->
                        uriBuilder
                                .path("/jobs/search")
                                .query("count={count}&page={page}")
                                .build(map))
                .retrieve()
                .bodyToFlux(Integer.class)
                .doOnNext(System.out::println);

        StepVerifier.create(flux)
                .expectNextCount(2)
                .verifyComplete();
    }
}
