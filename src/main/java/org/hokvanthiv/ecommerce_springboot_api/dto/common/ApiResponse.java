package org.hokvanthiv.ecommerce_springboot_api.dto.common;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ApiResponse<T> {

    private int statusCode;
    private String status;
    private String message;
    private T body;
    private LocalDateTime timestamp;

    // SUCCESS with data
    public static <T> ApiResponse<T> success(T body, String message) {
        ApiResponse<T> res = new ApiResponse<>();
        res.setStatusCode(200);
        res.setStatus("success");
        res.setMessage(message);
        res.setBody(body);
        res.setTimestamp(LocalDateTime.now());
        return res;
    }

    // SUCCESS no data
    public static <T> ApiResponse<T> success(String message) {
        ApiResponse<T> res = new ApiResponse<>();
        res.setStatusCode(200);
        res.setStatus("success");
        res.setMessage(message);
        res.setBody(null);
        res.setTimestamp(LocalDateTime.now());
        return res;
    }

    // ERROR response
    public static <T> ApiResponse<T> error(int statusCode, String message) {
        ApiResponse<T> res = new ApiResponse<>();
        res.setStatusCode(statusCode);
        res.setStatus("error");
        res.setMessage(message);
        res.setBody(null);
        res.setTimestamp(LocalDateTime.now());
        return res;
    }
}
