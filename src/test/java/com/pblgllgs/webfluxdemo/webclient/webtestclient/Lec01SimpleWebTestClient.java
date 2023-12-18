package com.pblgllgs.webfluxdemo.webclient.webtestclient;

import com.pblgllgs.webfluxdemo.dto.Response;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

/*
 *
 * @author pblgl
 * Created on 15-12-2023
 *
 */
@SpringBootTest
@AutoConfigureWebTestClient
class Lec01SimpleWebTestClient {

    @Autowired
    private WebTestClient client;

    @Test
    void stepVerifierTest() {
        Flux<Response> response = client
                .get()
                .uri("/reactive-math/square/{input}", 5)
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .returnResult(Response.class)
                .getResponseBody();

        StepVerifier.create(response)
                .expectNextMatches(r -> r.getOutput() == 25)
                .verifyComplete();
    }

    @Test
    void fluentAssertionTest() {
        client
                .get()
                .uri("/reactive-math/square/{input}", 5)
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(Response.class)
                .value(r -> Assertions.assertThat(r.getOutput()).isEqualTo(25));
    }
}
