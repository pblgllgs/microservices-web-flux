package com.pblgllgs.webfluxdemo.dto;

import lombok.Data;
import lombok.ToString;

import java.util.Date;

/*
 *
 * @author pblgl
 * Created on 05-12-2023
 *
 */
@Data
@ToString
public class Response {
    private Date date = new Date();
    private int output;

    public Response(int output) {
        this.output = output;
    }
}
