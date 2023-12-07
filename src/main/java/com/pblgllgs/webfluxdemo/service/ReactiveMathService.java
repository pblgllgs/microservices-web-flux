package com.pblgllgs.webfluxdemo.service;
/*
 *
 * @author pblgl
 * Created on 05-12-2023
 *
 */

import com.pblgllgs.webfluxdemo.dto.MultiplyRequestDto;
import com.pblgllgs.webfluxdemo.dto.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Map;
import java.util.Objects;

@Service
@Slf4j
public class ReactiveMathService {

    public Mono<Response> findSquare(int input) {
        return Mono
                .fromSupplier(() -> input * input)
                .map(Response::new);
    }

    public Flux<Response> multiplierTable(int input) {
        return Flux.range(1, 10)
                .delayElements(Duration.ofSeconds(1))
//                .doOnNext(i -> SleepUtil.sleepSeconds(1))
                .doOnNext(i -> log.info("reactive-math-service processing: " + i))
                .map(i -> new Response(i * input));
    }

    public Mono<Response> multiply(Mono<MultiplyRequestDto> monoDto){
        return monoDto
                .map(dto -> dto.getFirst() * dto.getSecond())
                .map(Response::new);
    }
}
