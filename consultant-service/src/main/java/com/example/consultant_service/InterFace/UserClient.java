package com.example.consultant_service.InterFace;

import com.example.consultant_service.Config.FeignConfig;
import com.example.consultant_service.Model.Response.AccountResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "user-service", url = "http://user-service:8082",configuration = FeignConfig.class)
public interface UserClient {

    @GetMapping("/api/users/{uuid}")
    AccountResponse getUserByUuid(@PathVariable("uuid") String uuid);

}

