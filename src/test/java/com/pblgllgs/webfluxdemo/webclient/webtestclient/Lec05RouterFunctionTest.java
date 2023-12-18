package com.pblgllgs.webfluxdemo.webclient.webtestclient;

import com.pblgllgs.webfluxdemo.RequestHandler;
import com.pblgllgs.webfluxdemo.config.RouterConfig;
import com.pblgllgs.webfluxdemo.dto.Response;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.ServerResponse;

/*
 *
 * @author pblgl
 * Created on 18-12-2023
 *
 */
@WebFluxTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ContextConfiguration(classes = RouterConfig.class)
class Lec05RouterFunctionTest {

    private WebTestClient webTestClient;

    @Autowired
    private ApplicationContext ctx;

    @MockBean
    private RequestHandler requestHandler;

    @BeforeAll
    void setClient() {
        webTestClient = WebTestClient.bindToApplicationContext(ctx).build();
    }

    @Test
    void test() {
        Mockito.when(
                requestHandler.squareHandler(Mockito.any())
        ).thenReturn(
                ServerResponse.ok().bodyValue(new Response(255)
                )
        );

        webTestClient
                .get()
                .uri("/router/square/{input}",15)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(Response.class)
                .value( r -> Assertions.assertThat(r.getOutput()).isEqualTo(255));

    }

}
