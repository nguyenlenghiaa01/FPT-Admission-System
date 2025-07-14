package com.fptu.hk7.candidateservice.InterFace;

import com.fptu.hk7.candidateservice.dto.response.AuthorResponse;

import javax.crypto.SecretKey;

public interface ITokenService {
    SecretKey getSigninKey();
    AuthorResponse isAuthenticated(String token);
}
