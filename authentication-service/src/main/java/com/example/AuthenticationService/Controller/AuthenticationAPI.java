package com.example.AuthenticationService.Controller;

import com.example.AuthenticationService.InterFace.IAuthenticationService;
import com.example.AuthenticationService.InterFace.UserClient;
import com.example.AuthenticationService.Model.Request.*;
import com.example.AuthenticationService.Model.Response.AccountResponse;
import com.example.AuthenticationService.Service.AuthenticationService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("api/auth")
@SecurityRequirement(name = "api")
@RequiredArgsConstructor
public class AuthenticationAPI {

    public final IAuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity <AccountResponse>register(@Valid @RequestBody RegisterRequest registerRequest) {
        AccountResponse newAccount = authenticationService.register(registerRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(newAccount);
    }


    @PostMapping("/login")
    public ResponseEntity <AccountResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        AccountResponse accountResponse = authenticationService.login(loginRequest);
        if (accountResponse == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(accountResponse);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        String token = authHeader.substring(7);
        return ResponseEntity.ok(authenticationService.logout(token));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@Valid @RequestBody ForgotPasswordRequest forgotPasswordRequest){
        authenticationService.forgotPassword(forgotPasswordRequest);
        return ResponseEntity.ok("forgot password successfully");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@Valid @RequestBody ResetPasswordRequest resetPasswordRequest){
        authenticationService.resetPassword(resetPasswordRequest);
        return ResponseEntity.ok("reset password successfully");
    }

    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@Valid @RequestBody ChangePasswordRequest changePasswordRequest) {
        String message = authenticationService.changePassword(changePasswordRequest);
        return ResponseEntity.ok(message);
    }


}

