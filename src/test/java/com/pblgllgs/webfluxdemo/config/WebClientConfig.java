package com.pblgllgs.webfluxdemo.config;
/*
 *
 * @author pblgl
 * Created on 07-12-2023
 *
 */

import com.pblgllgs.webfluxdemo.dto.InputFailedValidationResponse;
import com.pblgllgs.webfluxdemo.dto.MultiplyRequestDto;
import com.pblgllgs.webfluxdemo.dto.Response;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Map;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient webClient(){
        return WebClient.builder()
                .baseUrl("http://localhost:8080")
                .filter(this::sessionToken)
                .build();
    }


    private Mono<ClientResponse> sessionToken(ClientRequest request, ExchangeFunction ex){
        ClientRequest clientRequest = request.attribute("auth")
                .map(v -> v.equals("basic") ? withBasicAuth(request) : withOAuth(request))
                .orElse(request);
        return ex.exchange(clientRequest);
    }

    private ClientRequest withBasicAuth(ClientRequest request){
        return ClientRequest.from(request)
                .headers(h -> h.setBasicAuth("username","password"))
                .build();
    }

    private ClientRequest withOAuth(ClientRequest request){
        return ClientRequest.from(request)
                .headers(h -> h.setBearerAuth("jwt"))
                .build();
    }

    @Nested
    class Lec01GetSimpleResponseTest extends WebfluxDemoApplicationTests {

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

    @Nested
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

    @Nested
    class Lec03PostRequestTest extends WebfluxDemoApplicationTests{
        @Autowired
        private WebClient webClient;

        @Test
        void postTest(){

            Mono<Response> responseMono = webClient
                    .post()
                    .uri("reactive-math/multiply")
                    .bodyValue(buildRequestDto(5, 2))
                    .retrieve()
                    .bodyToMono(Response.class)
                    .doOnNext(System.out::println);

            StepVerifier.create(responseMono)
                    .expectNextCount(1)
                    .verifyComplete();

        }

        private MultiplyRequestDto buildRequestDto(int a, int b){
            MultiplyRequestDto dto = new MultiplyRequestDto();
            dto.setFirst(a);
            dto.setSecond(b);
            return dto;
        }
    }

    @Nested
    class Lec04HeadersTest extends WebfluxDemoApplicationTests{

        @Autowired
        private WebClient webClient;

        @Test
        void headersTest(){

            Mono<Response> responseMono = webClient
                    .post()
                    .uri("reactive-math/multiply")
                    .bodyValue(buildRequestDto(5, 2))
                    .headers(httpHeaders -> httpHeaders.set("someKey", "someValue"))
                    .retrieve()
                    .bodyToMono(Response.class)
                    .doOnNext(System.out::println);

            StepVerifier.create(responseMono)
                    .expectNextCount(1)
                    .verifyComplete();

        }

        private MultiplyRequestDto buildRequestDto(int a, int b){
            MultiplyRequestDto dto = new MultiplyRequestDto();
            dto.setFirst(a);
            dto.setSecond(b);
            return dto;
        }

    }

    @Nested
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

    @Nested
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

    @Nested
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

    @Nested
    class Lec08AttributesTest extends WebfluxDemoApplicationTests {

        @Autowired
        private WebClient webClient;

        @Test
        void headersOAuthTest(){

            Mono<Response> responseMono = webClient
                    .post()
                    .uri("reactive-math/multiply")
                    .bodyValue(buildRequestDto(5, 2))
                    .attribute("auth", "oauth")
                    .retrieve()
                    .bodyToMono(Response.class)
                    .doOnNext(System.out::println);

            StepVerifier.create(responseMono)
                    .expectNextCount(1)
                    .verifyComplete();

        }

        @Test
        void headersAuthBasicTest(){

            Mono<Response> responseMono = webClient
                    .post()
                    .uri("reactive-math/multiply")
                    .bodyValue(buildRequestDto(5, 2))
                    .attribute("auth", "basic")
                    .retrieve()
                    .bodyToMono(Response.class)
                    .doOnNext(System.out::println);

            StepVerifier.create(responseMono)
                    .expectNextCount(1)
                    .verifyComplete();

        }

        private MultiplyRequestDto buildRequestDto(int a, int b){
            MultiplyRequestDto dto = new MultiplyRequestDto();
            dto.setFirst(a);
            dto.setSecond(b);
            return dto;
        }
    }

    @Nested
    class Lec09AssigmentTest extends WebfluxDemoApplicationTests {

        private final static String FORMAT = "%d %s %d = %s";
        public static final int A = 10;

        @Autowired
        private WebClient webClient;

        @Test
        void test() {

            Flux<String> flux = Flux.range(1, 5)
                    .flatMap(
                            b ->
                                    Flux
                                            .just("+", "-", "*", "/")
                                            .flatMap(
                                                    op ->
                                                            send(b, op)
                                            )
                    )
                    .doOnNext(System.out::println);

            StepVerifier.create(flux)
                    .expectNextCount(20)
                    .verifyComplete();

        }

        private Mono<String> send(int b, String op) {
            return webClient.get()
                    .uri("calculator/{a}/{b}", A, b)
                    .headers(h -> h.set("OP", op))
                    .retrieve()
                    .bodyToMono(String.class)
                    .map(v -> String.format(FORMAT, A, op, b, v));
        }

    }

    @SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
    static
    class WebfluxDemoApplicationTests {

    }
}
