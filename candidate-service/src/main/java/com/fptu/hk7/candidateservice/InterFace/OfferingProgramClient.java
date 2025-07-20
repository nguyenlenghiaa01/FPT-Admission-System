package com.fptu.hk7.candidateservice.InterFace;

import com.fptu.hk7.candidateservice.dto.request.FindOfferingRequest;
import com.fptu.hk7.candidateservice.dto.response.GetOfferingResponse;
import com.fptu.hk7.candidateservice.event.OfferingDetail;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.UUID;

@FeignClient(value = "program-service", url = "${feign.client.program-service.url}")
public interface OfferingProgramClient {
    @PostMapping(value = "/api/program/get_offering")
    ResponseEntity<GetOfferingResponse> getOffering(@RequestBody FindOfferingRequest findOfferingRequest);

    @GetMapping("/api/program/get_detail_offering/{id}")
    ResponseEntity<OfferingDetail> getOfferingDetail(@PathVariable("id") UUID id);
}
