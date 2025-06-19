package com.example.AuthenticationService.Controller;

import com.example.AuthenticationService.Model.Request.ChangePasswordRequest;
import com.example.AuthenticationService.Model.Request.LoginRequest;
import com.example.AuthenticationService.Model.Request.RegisterRequest;
import com.example.AuthenticationService.Model.Request.ResetPasswordRequest;
import com.example.AuthenticationService.Model.Response.AccountResponse;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class UserClientFallback implements UserClient {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(UserClientFallback.class);

    @Override
    public AccountResponse register(RegisterRequest registerRequest) {
        logger.error("User service is unavailable - register fallback");

        AccountResponse response = new AccountResponse();
        response.setMessage("Fallback: service unavailable");
        return response;
    }

    @Override
    public AccountResponse login(LoginRequest loginRequest) {
        logger.error("User service is unavailable - login fallback");
        AccountResponse response = new AccountResponse();
        response.setMessage("Fallback: service unavailable");
        return response;
    }

    @Override
    public AccountResponse getAccountByEmail(String email) {
        logger.error("User service is unavailable - getAccountByEmail fallback");
        AccountResponse response = new AccountResponse();
        response.setMessage("Fallback: service unavailable");
        return response;
    }

    @Override
    public String changePassword(ChangePasswordRequest changePasswordRequest) {
        logger.error("User service is unavailable - changePassword fallback");
        return "Fail: Service unavailable";
    }

    @Override
    public void resetPassword(ResetPasswordRequest resetPasswordRequest) {
        logger.error("User service is unavailable - resetPassword fallback");
    }
}
