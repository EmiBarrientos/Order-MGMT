package com.ordermgmt.orders.exceptions.custom;

public class OrderNotFoundException extends RuntimeException {
    public OrderNotFoundException(Long id) {
        super("Could not find order with id " + id);
    }
}
