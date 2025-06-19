package com.fptu.hk7.candidateservice.client;

import com.fptu.hk7.candidateservice.dto.response.AccountResponse;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;

@Component
public class UserClientFallback implements UserClient {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(UserClientFallback.class);

    @Override
    public AccountResponse getAccountByEmail(@RequestParam String email) {
        logger.error("Candidate service is unavailable - get account fallback");

        AccountResponse response = new AccountResponse();
        response.setMessage("Fallback: service unavailable");
        return response;
    }

}
