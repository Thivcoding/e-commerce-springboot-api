package org.hokvanthiv.ecommerce_springboot_api.exception;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class BadRequestException extends RuntimeException {

    private final HttpStatus status;
    private final String message;

    public BadRequestException(String message) {
        super(message);
        this.status = HttpStatus.BAD_REQUEST;
        this.message = message;
    }
}