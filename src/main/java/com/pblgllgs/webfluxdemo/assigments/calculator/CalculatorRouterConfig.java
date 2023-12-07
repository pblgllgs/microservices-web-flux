package com.pblgllgs.webfluxdemo.assigments.calculator;
/*
 *
 * @author pblgl
 * Created on 07-12-2023
 *
 */

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.*;

@Configuration
public class CalculatorRouterConfig {

    private final CalculatorHandler calculatorHandler;

    public CalculatorRouterConfig(CalculatorHandler calculatorHandler) {
        this.calculatorHandler = calculatorHandler;
    }

    @Bean
    public RouterFunction<ServerResponse> highLevelCalculatorRouter() {
        return RouterFunctions.route()
                .path("calculator", this::serverResponseRouterFunction)
                .build();
    }

    private RouterFunction<ServerResponse> serverResponseRouterFunction() {
        return RouterFunctions.route()
                .GET("{a}/{b}", isOperation("+"), calculatorHandler::sum)
                .GET("{a}/{b}", isOperation("-"), calculatorHandler::min)
                .GET("{a}/{b}", isOperation("*"), calculatorHandler::mult)
                .GET("{a}/{b}", isOperation("/"), calculatorHandler::div)
                .GET("{a}/{b}", req -> ServerResponse.badRequest().bodyValue("OP should be [+,-,/,*]"))
                .build();
    }

    private RequestPredicate isOperation(String operation) {
        return RequestPredicates.headers(headers -> operation.equals(headers.asHttpHeaders().toSingleValueMap().get("OP")));
    }
}
