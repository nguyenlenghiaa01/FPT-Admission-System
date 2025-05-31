package com.example.AuthenticationService.Exception;

public class DuplicateEntity extends RuntimeException{
    public DuplicateEntity(String message){
        super(message);
    }
}
