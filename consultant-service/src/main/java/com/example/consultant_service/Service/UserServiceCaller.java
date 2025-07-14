package com.example.consultant_service.Service;

import com.example.consultant_service.InterFace.UserClient;
import com.example.consultant_service.Model.Response.AccountResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceCaller {

    @Autowired
    private UserClient userClient;

    @CircuitBreaker(name = "userGetCB", fallbackMethod = "getFallback")
    public AccountResponse getUserByUuid(String uuid) {
        return userClient.getUserByUuid(uuid);
    }

    public AccountResponse getFallback(String uuid, Throwable t) {
        System.out.println(" Fallback get user triggered: " + t.getMessage());
        AccountResponse response = new AccountResponse();
        response.setMessage("Fallback: user-service unavailable");
        return response;
    }

}
