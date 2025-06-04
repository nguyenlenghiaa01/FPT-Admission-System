package com.fptu.hk7.candidateservice.client;

import com.fptu.hk7.candidateservice.dto.response.AccountResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "user-service", url = "${feign.client.user-service.url}")
public interface UserClient {
    @GetMapping("/api/users/getEmail")
    AccountResponse getAccountByEmail(@RequestParam String email);
}
