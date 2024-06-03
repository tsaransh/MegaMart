package com.web.megamart.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ProductReviewException extends RuntimeException {

    private String message;
    private HttpStatus httpStatus;

    public ProductReviewException(String message, HttpStatus httpStatus) {
        super(message);
        this.message = message;
        this.httpStatus = httpStatus;
    }

}
