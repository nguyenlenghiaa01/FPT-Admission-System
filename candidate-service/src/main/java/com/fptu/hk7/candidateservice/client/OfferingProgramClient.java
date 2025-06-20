package com.fptu.hk7.candidateservice.client;

import com.fptu.hk7.candidateservice.dto.request.FindOfferingRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.UUID;

@FeignClient(value = "program-service", url = "${feign.client.program-service.url}",fallback = OfferingProgramClient.class)
public interface OfferingProgramClient {
    @PostMapping(value = "/api/program/get_offering")
    ResponseEntity<UUID> getOfferingByCampusNameAndSpecializationName(@RequestBody FindOfferingRequest findOfferingRequest);
}
