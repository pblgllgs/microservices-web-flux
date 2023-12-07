package com.pblgllgs.webfluxdemo.controller;
/*
 *
 * @author pblgl
 * Created on 05-12-2023
 *
 */

import com.pblgllgs.webfluxdemo.dto.Response;
import com.pblgllgs.webfluxdemo.service.MathService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/math")
public class MathController {

    private final MathService mathService;

    public MathController(MathService mathService) {
        this.mathService = mathService;
    }

    @GetMapping("/square/{input}")
    public ResponseEntity<Response>  findSquare(@PathVariable("input") int input){
        return new ResponseEntity<>(mathService.findSquare(input), HttpStatus.OK);
    }

    @GetMapping("/table/{input}")
    public ResponseEntity<List<Response>> findAll(@PathVariable("input") int input){
        return new ResponseEntity<>(mathService.multiplicationTable(input), HttpStatus.OK);
    }
}
