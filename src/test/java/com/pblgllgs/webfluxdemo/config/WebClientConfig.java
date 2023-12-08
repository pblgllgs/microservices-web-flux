package com.pblgllgs.webfluxdemo.config;
/*
 *
 * @author pblgl
 * Created on 07-12-2023
 *
 */

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

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
}