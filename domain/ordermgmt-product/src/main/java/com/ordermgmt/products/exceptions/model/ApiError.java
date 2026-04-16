package com.ordermgmt.products.exceptions.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ApiError {
    private int status;
    private String error;
    private String message;
    private LocalDateTime timestamp;

    // constructor
    public ApiError(int status, String error, String message) {
        this.status = status;
        this.error = error;
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }
}
