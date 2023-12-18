package com.pblgllgs.webfluxdemo.webclient.webtestclient;
/*
 *
 * @author pblgl
 * Created on 18-12-2023
 *
 */

import com.pblgllgs.webfluxdemo.controller.ReactiveMathController;
import com.pblgllgs.webfluxdemo.dto.MultiplyRequestDto;
import com.pblgllgs.webfluxdemo.dto.Response;
import com.pblgllgs.webfluxdemo.service.ReactiveMathService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

@WebFluxTest(ReactiveMathController.class)
class Lec03ControllerPostTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private ReactiveMathService reactiveMathService;

    @Test
    void postTest(){

        Mockito.when(reactiveMathService.multiply(Mockito.any())).thenReturn(Mono.just(new Response(1)));

        webTestClient
                .post()
                .uri("/reactive-math/multiply")
                .accept(MediaType.APPLICATION_JSON)
                .headers(h -> h.setBasicAuth("username","password"))
                .headers(h -> h.set("somekey","somevalue"))
                .bodyValue( new MultiplyRequestDto())
                .exchange()
                .expectStatus().is2xxSuccessful();
    }
}
