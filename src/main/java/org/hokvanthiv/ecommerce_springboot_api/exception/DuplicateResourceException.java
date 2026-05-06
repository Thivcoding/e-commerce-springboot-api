package org.hokvanthiv.ecommerce_springboot_api.exception;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class DuplicateResourceException extends RuntimeException {

    private final HttpStatus status;
    private final String message;

    public DuplicateResourceException(String message) {
        super(message);
        this.status = HttpStatus.CONFLICT;
        this.message = message;
    }
}
