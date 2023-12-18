package com.pblgllgs.webfluxdemo.webclient.webtestclient;

import com.pblgllgs.webfluxdemo.controller.ReactiveMathValidationController;
import com.pblgllgs.webfluxdemo.dto.Response;
import com.pblgllgs.webfluxdemo.service.ReactiveMathService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

/*
 *
 * @author pblgl
 * Created on 18-12-2023
 *
 */
@WebFluxTest(ReactiveMathValidationController.class)
class Lec04ErrorHandlingTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private ReactiveMathService reactiveMathService;

    @Test
    void errorHandlingTest() {

        Mockito.when(reactiveMathService.findSquare(Mockito.anyInt())).thenReturn(Mono.just(new Response(1)));

        webTestClient
                .get()
                .uri("/reactive-math-valid/square/{input}/throw", 5)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.message").isEqualTo("allowed range is 10 - 20")
                .jsonPath("$.errorCode").isEqualTo(100)
                .jsonPath("$.input").isEqualTo(5);


    }
}
