package com.pblgllgs.webfluxdemo.assigments.calculator;
/*
 *
 * @author pblgl
 * Created on 07-12-2023
 *
 */

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.function.BiFunction;

@Service
public class CalculatorHandler {

    public Mono<ServerResponse> sum(ServerRequest request) {
        return process(request, (a, b) -> ServerResponse.ok().bodyValue(a + b));
    }

    public Mono<ServerResponse> min(ServerRequest request) {
        return process(request, (a, b) -> ServerResponse.ok().bodyValue(a - b));
    }

    public Mono<ServerResponse> mult(ServerRequest request) {
        return process(request, (a, b) -> ServerResponse.ok().bodyValue(a * b));
    }

    public Mono<ServerResponse> div(ServerRequest request) {
        return
                process(request, (a, b) ->
                        b != 0
                                ?
                                ServerResponse.ok().bodyValue(a / b)
                                :
                                ServerResponse.badRequest().bodyValue("B cant not be 0!")
                );
    }

    public Mono<ServerResponse> process(ServerRequest request, BiFunction<Integer, Integer, Mono<ServerResponse>> operationLogic) {
        int a = getValue(request, "a");
        int b = getValue(request, "b");
        return operationLogic.apply(a, b);
    }

    private int getValue(ServerRequest request, String key) {
        return Integer.parseInt(request.pathVariable(key));
    }
}
