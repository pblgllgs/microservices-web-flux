package com.pblgllgs.webfluxdemo;
/*
 *
 * @author pblgl
 * Created on 07-12-2023
 *
 */

import com.pblgllgs.webfluxdemo.dto.MultiplyRequestDto;
import com.pblgllgs.webfluxdemo.dto.Response;
import com.pblgllgs.webfluxdemo.exception.InputValidationException;
import com.pblgllgs.webfluxdemo.service.ReactiveMathService;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class RequestHandler {

    private final ReactiveMathService reactiveMathService;

    public RequestHandler(ReactiveMathService reactiveMathService) {
        this.reactiveMathService = reactiveMathService;
    }

    public Mono<ServerResponse> squareHandler(ServerRequest serverRequest) {
        int input = Integer.parseInt(serverRequest.pathVariable("input"));
        Mono<Response> square = reactiveMathService.findSquare(input);
        return ServerResponse.ok().body(square, Response.class);
    }

    public Mono<ServerResponse> tableHandler(ServerRequest serverRequest) {
        int input = Integer.parseInt(serverRequest.pathVariable("input"));
        Flux<Response> table = reactiveMathService.multiplierTable(input);
        return ServerResponse.ok().body(table, Response.class);
    }

    public Mono<ServerResponse> tableHandlerStream(ServerRequest serverRequest) {
        int input = Integer.parseInt(serverRequest.pathVariable("input"));
        Flux<Response> table = reactiveMathService.multiplierTable(input);
        return ServerResponse.ok()
                .contentType(MediaType.TEXT_EVENT_STREAM)
                .body(table, Response.class);
    }

    public Mono<ServerResponse> multiplyHandler(ServerRequest serverRequest) {
        Mono<MultiplyRequestDto> requestDtoMono = serverRequest.bodyToMono(MultiplyRequestDto.class);
        Mono<Response> multiplyResponse = reactiveMathService.multiply(requestDtoMono);
        return ServerResponse.ok()
                .body(multiplyResponse, Response.class);
    }

    public Mono<ServerResponse> squareHandlerWithValidation(ServerRequest serverRequest) {
        int input = Integer.parseInt(serverRequest.pathVariable("input"));
        if (input < 10 || input > 20) {
            return Mono.error(new InputValidationException(input));
        }
        Mono<Response> square = reactiveMathService.findSquare(input);
        return ServerResponse.ok().body(square, Response.class);
    }


}
