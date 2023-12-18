package com.pblgllgs.webfluxdemo.webclient.webtestclient;

import com.pblgllgs.webfluxdemo.controller.ParamsController;
import com.pblgllgs.webfluxdemo.controller.ReactiveMathController;
import com.pblgllgs.webfluxdemo.dto.Response;
import com.pblgllgs.webfluxdemo.service.ReactiveMathService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Map;

/*
 *
 * @author pblgl
 * Created on 15-12-2023
 *
 */
@WebFluxTest(controllers = {ReactiveMathController.class, ParamsController.class})
class Lec02ControllerGetTest {

    @Autowired
    private WebTestClient client;

    @MockBean
    private ReactiveMathService reactiveMathService;

    @Test
    void singleResponseTest() {

        Mockito.when(reactiveMathService.findSquare(Mockito.anyInt())).thenReturn(Mono.just(new Response(25)));

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

    @Test
    void listResponseTest() {

        Flux<Response> flux = Flux.range(1, 3)
                .map(Response::new);

        Mockito.when(reactiveMathService.multiplierTable(Mockito.anyInt())).thenReturn(flux);

        client
                .get()
                .uri("/reactive-math/table/{input}", 5)
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(Response.class)
                .hasSize(3);
    }

    @Test
    void streamingResponseTest() {

        Flux<Response> flux = Flux.range(1, 3)
                .map(Response::new)
                .delayElements(Duration.ofMillis(100));

        Mockito.when(reactiveMathService.multiplierTable(Mockito.anyInt())).thenReturn(flux);

        client
                .get()
                .uri("/reactive-math/table/{input}/stream", 5)
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectHeader().contentTypeCompatibleWith(MediaType.TEXT_EVENT_STREAM)
                .expectBodyList(Response.class)
                .hasSize(3);
    }

    @Test
    void paramsTest() {

        Map<String, Integer> map = Map.of(
                "count", 20, "page", 10
        );

        this.client
                .get()
                .uri(uriBuilder ->
                        uriBuilder
                                .path("/jobs/search")
                                .query("count={count}&page={page}")
                                .build(map))
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBodyList(Integer.class)
                .hasSize(2).contains(10, 20);
    }

}
