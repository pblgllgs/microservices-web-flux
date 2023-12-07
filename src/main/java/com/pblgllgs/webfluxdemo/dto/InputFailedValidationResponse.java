package com.pblgllgs.webfluxdemo.dto;
/*
 *
 * @author pblgl
 * Created on 05-12-2023
 *
 */

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class InputFailedValidationResponse {

    private int errorCode;
    private int input;
    private String message;
}
