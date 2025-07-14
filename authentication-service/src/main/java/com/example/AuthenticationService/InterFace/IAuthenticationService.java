package com.example.AuthenticationService.InterFace;

import com.example.AuthenticationService.Model.Request.*;
import com.example.AuthenticationService.Model.Response.AccountResponse;

public interface IAuthenticationService {
    AccountResponse register(RegisterRequest registerRequest);

    AccountResponse login(LoginRequest loginRequest);

    void forgotPassword(ForgotPasswordRequest forgotPasswordRequest);

    void resetPassword(ResetPasswordRequest resetPasswordRequest);

    String changePassword(ChangePasswordRequest changePasswordRequest);

    String logout(String token);
}
