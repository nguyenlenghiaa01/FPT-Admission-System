package com.example.AuthenticationService.Controller;

import com.example.AuthenticationService.Model.Request.*;
import com.example.AuthenticationService.Model.Response.AccountResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserClientFallback {

    @Autowired
    private UserClient userClient;

    @CircuitBreaker(name = "userLoginCB", fallbackMethod = "loginFallback")
    public AccountResponse login(LoginRequest loginRequest) {
        return userClient.login(loginRequest);
    }

    public AccountResponse loginFallback(LoginRequest loginRequest, Throwable t) {
        System.out.println(" Fallback login triggered: " + t.getMessage());
        AccountResponse response = new AccountResponse();
        response.setMessage("Fallback: user-service unavailable");
        return response;
    }

    @CircuitBreaker(name = "userRegisterCB", fallbackMethod = "registerFallback")
    public AccountResponse register(RegisterRequest request) {
        return userClient.register(request);
    }

    public AccountResponse registerFallback(RegisterRequest request, Throwable t) {
        AccountResponse response = new AccountResponse();
        response.setMessage("Fallback: user-service unavailable (register)");
        return response;
    }

    @CircuitBreaker(name = "userEmailCB", fallbackMethod = "getAccountByEmailFallback")
    public AccountResponse getAccountByEmail(String email) {
        return userClient.getAccountByEmail(email);
    }

    public AccountResponse getAccountByEmailFallback(String email, Throwable t) {
        AccountResponse response = new AccountResponse();
        response.setMessage("Fallback: user-service unavailable (getEmail)");
        return response;
    }

    @CircuitBreaker(name = "userChangePassCB", fallbackMethod = "changePasswordFallback")
    public String changePassword(ChangePasswordRequest request) {
        return userClient.changePassword(request);
    }

    public String changePasswordFallback(ChangePasswordRequest request, Throwable t) {
        return "Fallback: user-service unavailable (change-password)";
    }

    @CircuitBreaker(name = "userResetPassCB", fallbackMethod = "resetPasswordFallback")
    public void resetPassword(ResetPasswordRequest request) {
        userClient.resetPassword(request);
    }

    public void resetPasswordFallback(ResetPasswordRequest request, Throwable t) {
        System.out.println("Fallback: user-service unavailable (reset-password)");
    }
}
