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
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class Lec01GetSimpleResponseTest extends WebfluxDemoApplicationTests{

    @Autowired
    private WebClient webClient;

    @Test
    public void blockTest(){
        Response response = webClient
                .get()
                .uri("reactive-math/square/{input}", 5)
                .retrieve()
                .bodyToMono(Response.class)
                .block();
        System.out.println(response);
    }

    @Test
    public void stepVerifierTest(){
        Mono<Response> response = webClient
                .get()
                .uri("reactive-math/square/{input}", 5)
                .retrieve()
                .bodyToMono(Response.class);

        StepVerifier.create(response)
                .expectNextMatches(r -> r.getOutput() == 25)
                .verifyComplete();
    }
}
