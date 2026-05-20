package org.hokvanthiv.ecommerce_springboot_api.exception;

public class AccessDeniedException extends RuntimeException{
    public AccessDeniedException(String message){
        super(message);
    }
}
