package com.ecommers.product.exceptions;

public class BadCreateRequest extends RuntimeException{
    public BadCreateRequest(String message){
        super(message);
    }
}
