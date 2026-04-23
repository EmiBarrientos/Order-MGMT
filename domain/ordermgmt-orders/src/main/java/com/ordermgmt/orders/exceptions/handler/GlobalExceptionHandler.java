package com.ordermgmt.orders.exceptions.handler;

import com.ordermgmt.orders.exceptions.custom.ExternalServiceException;
import com.ordermgmt.orders.exceptions.custom.OrderNotFoundException;
import com.ordermgmt.orders.exceptions.model.ApiError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<ApiError> handleOrderNotFound(OrderNotFoundException ex) {

        ApiError error = new ApiError(
                404,
                "NOT_FOUND",
                ex.getMessage()
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiError> handleRuntime(RuntimeException ex) {

        ApiError error = new ApiError(
                400,
                "BAD_REQUEST",
                ex.getMessage()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGeneric(Exception ex) {

        ApiError error = new ApiError(
                500,
                "INTERNAL_SERVER_ERROR",
                "Unexpected error"
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

    @ExceptionHandler(ExternalServiceException.class)
    public ResponseEntity<ApiError> handleExternalService(ExternalServiceException ex) {

        ApiError error = new ApiError(
                502,
                "BAD_GATEWAY",
                ex.getMessage()
        );

        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(error);
    }
}
