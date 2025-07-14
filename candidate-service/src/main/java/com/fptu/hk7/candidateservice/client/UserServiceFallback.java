package com.fptu.hk7.candidateservice.client;

import com.fptu.hk7.candidateservice.InterFace.UserClient;
import com.fptu.hk7.candidateservice.dto.response.AccountResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceFallback {

    @Autowired
    private UserClient userClient;


    @CircuitBreaker(name = "userEmailCB", fallbackMethod = "getAccountByEmailFallback")
    public AccountResponse getAccountByEmail(String email) {
        return userClient.getAccountByEmail(email);
    }

    public AccountResponse getAccountByEmailFallback(String email, Throwable t) {
        AccountResponse response = new AccountResponse();
        response.setMessage("Fallback: user-service unavailable (getEmail)");
        return response;
    }

}
