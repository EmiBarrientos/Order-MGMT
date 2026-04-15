package com.ordermgmt.products.exceptions;

public class ProductNotFoundException extends RuntimeException
{
    public ProductNotFoundException(String message)
    {
        super(message);
    }
}
