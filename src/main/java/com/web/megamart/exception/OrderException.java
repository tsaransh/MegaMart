package com.web.megamart.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class OrderException extends RuntimeException {

    private String message;
    private HttpStatus HttpStatus;

    public OrderException(String message, HttpStatus httpStatus) {
        super(message);
        this.message = message;
        this.HttpStatus = httpStatus;
    }
}
