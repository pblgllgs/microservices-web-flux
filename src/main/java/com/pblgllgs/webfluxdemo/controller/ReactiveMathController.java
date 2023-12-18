package com.pblgllgs.webfluxdemo.controller;
/*
 *
 * @author pblgl
 * Created on 05-12-2023
 *
 */

import com.pblgllgs.webfluxdemo.dto.MultiplyRequestDto;
import com.pblgllgs.webfluxdemo.dto.Response;
import com.pblgllgs.webfluxdemo.service.ReactiveMathService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@RequestMapping("/reactive-math")
@Slf4j
public class ReactiveMathController {

    private final ReactiveMathService reactiveMathService;

    public ReactiveMathController(ReactiveMathService reactiveMathService) {
        this.reactiveMathService = reactiveMathService;
    }

    @GetMapping("/square/{input}")
    public ResponseEntity<Mono<Response>> findSquare(@PathVariable("input") int input) {
        return new ResponseEntity<>(reactiveMathService.findSquare(input), HttpStatus.OK);
    }

    @GetMapping("/table/{input}")
    public Flux<Response> multiplicationTable(@PathVariable("input") int input) {
        return reactiveMathService.multiplierTable(input);
    }

    @GetMapping(value = "/table/{input}/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public ResponseEntity<Flux<Response>> multiplicationTableStream(@PathVariable("input") int input) {
        return new ResponseEntity<>(reactiveMathService.multiplierTable(input), HttpStatus.OK);
    }

    @PostMapping("/multiply")
    public Mono<Response> multiply(
            @RequestBody Mono<MultiplyRequestDto> multiRequestDto,
            @RequestHeader Map<String, String> headers
    ) {
        log.info(headers.toString());
        return reactiveMathService.multiply(multiRequestDto);
    }
}
