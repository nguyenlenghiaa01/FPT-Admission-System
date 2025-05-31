package com.example.apigateway.Exception;

public class DuplicateEntity extends RuntimeException{
    public DuplicateEntity(String message){
        super(message);
    }
}
