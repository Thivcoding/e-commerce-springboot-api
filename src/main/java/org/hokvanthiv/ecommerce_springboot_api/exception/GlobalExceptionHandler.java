package org.hokvanthiv.ecommerce_springboot_api.exception;

import org.hokvanthiv.ecommerce_springboot_api.dto.common.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // =========================
    // 404 NOT FOUND
    // =========================
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<?>> handleNotFound(ResourceNotFoundException ex){

        ApiResponse<?> response = ApiResponse.error(
                404,
                ex.getMessage()
        );

        response.setTimestamp(LocalDateTime.now());

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }


    // =========================
    // 400 BAD REQUEST
    // =========================
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiResponse<?>> handleBadRequest(BadRequestException ex){

        ApiResponse<?> response = ApiResponse.error(
                400,
                ex.getMessage()
        );

        response.setTimestamp(LocalDateTime.now());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }


    // =========================
    // 409 CONFLICT (DUPLICATE)
    // =========================
    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ApiResponse<?>> handleDuplicate(DuplicateResourceException ex){

        ApiResponse<?> response = ApiResponse.error(
                409,
                ex.getMessage()
        );

        response.setTimestamp(LocalDateTime.now());

        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }


    // =========================
    // 500 INTERNAL SERVER ERROR
    // =========================
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> handleGeneralException(Exception ex){

        ApiResponse<?> response = ApiResponse.error(
                500,
                "Internal server error"
        );

        response.setTimestamp(LocalDateTime.now());

        // optional: log error
        ex.printStackTrace();

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}