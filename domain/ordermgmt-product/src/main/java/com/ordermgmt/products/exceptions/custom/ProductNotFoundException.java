package com.ordermgmt.products.exceptions.custom;

public class ProductNotFoundException extends RuntimeException
{
    public ProductNotFoundException(String message)
    {
        super(message);
    }
}
