package com.example.AuthenticationService.InterFace;

import com.example.AuthenticationService.Model.Response.AccountResponse;
import io.jsonwebtoken.Claims;

public interface ITokenService {
    String generateToken(AccountResponse account);
    Claims extractAllClaims(String token);
}
