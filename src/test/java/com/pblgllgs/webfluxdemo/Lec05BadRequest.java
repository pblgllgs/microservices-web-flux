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
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

class Lec05BadRequest extends WebfluxDemoApplicationTests{

    @Autowired
    private WebClient webClient;

    @Test
    void badRequestTest(){

        Mono<Response> responseMono = webClient
                .get()
                .uri("reactive-math-valid/square/{input}/throw", 5)
                .retrieve()
                .bodyToMono(Response.class)
                .doOnNext(System.out::println)
                .doOnError(err -> System.out.println(err.getMessage()));


        StepVerifier.create(responseMono)
                .verifyError(WebClientResponseException.BadRequest.class);
    }

}
