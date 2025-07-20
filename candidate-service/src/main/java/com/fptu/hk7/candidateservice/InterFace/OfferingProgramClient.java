package com.fptu.hk7.candidateservice.InterFace;

import com.fptu.hk7.candidateservice.dto.request.FindOfferingRequest;
import com.fptu.hk7.candidateservice.dto.response.GetOfferingResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "program-service", url = "${feign.client.program-service.url}")
public interface OfferingProgramClient {
    @PostMapping(value = "/api/program/get_offering")
    ResponseEntity<GetOfferingResponse> getOffering(@RequestBody FindOfferingRequest findOfferingRequest);
}
