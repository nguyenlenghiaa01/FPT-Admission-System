package com.fptu.hk7.blogservice.InterFace;

import com.fptu.hk7.blogservice.dto.AuthorResponse;

import javax.crypto.SecretKey;

public interface ITokenService {
    SecretKey getSigninKey();
    AuthorResponse isAuthenticated(String token);
}
