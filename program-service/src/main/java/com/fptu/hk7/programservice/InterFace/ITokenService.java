package com.fptu.hk7.programservice.InterFace;

import io.jsonwebtoken.Claims;

import javax.crypto.SecretKey;

public interface ITokenService {
    SecretKey getSigninKey();
    Claims extractAllClaims(String token);
}
