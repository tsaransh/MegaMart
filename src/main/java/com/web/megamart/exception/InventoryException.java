package com.web.megamart.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class InventoryException extends RuntimeException {
    private HttpStatus httpStatus;
    private String message;

    public InventoryException(String message, HttpStatus httpStatus) {
        super(message);
        this.message = message;
        this.httpStatus = httpStatus;
    }
}
