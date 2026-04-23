package com.ordermgmt.orders.exceptions.custom;

public class ExternalServiceException extends RuntimeException {
    public ExternalServiceException(String message) {
        super(message);
    }
}
