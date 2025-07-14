package com.example.consultant_service.InterFace;

import com.example.consultant_service.Entity.UserPrincipal;

import javax.crypto.SecretKey;

public interface ITokenService {
    SecretKey getSigningKey();
    UserPrincipal parseToken(String token);
}
