package com.example.AuthenticationService.Controller;

import com.example.AuthenticationService.Config.FeignConfig;
import com.example.AuthenticationService.Model.Request.ChangePasswordRequest;
import com.example.AuthenticationService.Model.Request.LoginRequest;
import com.example.AuthenticationService.Model.Request.RegisterRequest;
import com.example.AuthenticationService.Model.Request.ResetPasswordRequest;
import com.example.AuthenticationService.Model.Response.AccountResponse;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "user-service", url = "http://user-service:8082",configuration = FeignConfig.class,fallback = UserClientFallback.class)
public interface UserClient {
    @PostMapping("/api/users/register")
    AccountResponse register(@Valid @RequestBody RegisterRequest registerRequest);
    @PostMapping("/api/users/login")
    AccountResponse login(@Valid @RequestBody LoginRequest loginRequest);
    @GetMapping("/api/users/getEmail")
    AccountResponse getAccountByEmail(@RequestParam String email);

    @PostMapping("/api/users/change-password")
    String changePassword(@Valid @RequestBody ChangePasswordRequest changePasswordRequest);
    @PostMapping("/api/users/reset-password")
    void resetPassword(@Valid @RequestBody ResetPasswordRequest resetPasswordRequest);
}

