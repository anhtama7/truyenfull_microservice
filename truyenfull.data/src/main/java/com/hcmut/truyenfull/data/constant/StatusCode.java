/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hcmut.truyenfull.data.constant;

import org.springframework.http.HttpStatus;

/**
 *
 * @author TAM
 */
public enum StatusCode {
    NONE(0),
    SUCCESS(HttpStatus.OK.value()),
    SEVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR.value()),
    NOT_FOUND(HttpStatus.NOT_FOUND.value()),
    PARAMETER_INVALID(1000);
    
    private int value;

    private StatusCode(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
