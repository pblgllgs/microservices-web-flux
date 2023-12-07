package com.pblgllgs.webfluxdemo.controller;
/*
 *
 * @author pblgl
 * Created on 05-12-2023
 *
 */

import com.pblgllgs.webfluxdemo.dto.Response;
import com.pblgllgs.webfluxdemo.exception.InputValidationException;
import com.pblgllgs.webfluxdemo.service.ReactiveMathService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/reactive-math-valid")
@Slf4j
public class ReactiveMathValidationController {

    private final ReactiveMathService reactiveMathService;

    public ReactiveMathValidationController(ReactiveMathService reactiveMathService) {
        this.reactiveMathService = reactiveMathService;
    }

    @GetMapping("/square/{input}/throw")
    public ResponseEntity<Mono<Response>> findSquare(@PathVariable("input") int input) {
        if (input < 10 || input > 100) {
            throw new InputValidationException(input);
        }
        return new ResponseEntity<>(reactiveMathService.findSquare(input), HttpStatus.OK);
    }

    @GetMapping("/square/{input}/mono-error")
    public Mono<Response> monoError(@PathVariable("input") int input) {
        return Mono.just(input)
                .handle((integer, sink) -> {
                    if (integer >= 10 && integer <= 20) {
                        sink.next(integer);
                    } else {
                        sink.error(new InputValidationException(integer));
                    }
                })
                .cast(Integer.class)
                .flatMap(reactiveMathService::findSquare);
    }

    @GetMapping("/square/{input}/assignment")
    public Mono<ResponseEntity<Response>> assignment(@PathVariable("input") int input) {
        return Mono.just(input)
                .filter(i -> i >= 10 && i <= 20)
                .flatMap(reactiveMathService::findSquare)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.badRequest().build());
    }
}
