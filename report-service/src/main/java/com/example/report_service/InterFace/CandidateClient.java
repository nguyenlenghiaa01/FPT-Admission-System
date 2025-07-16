package com.example.report_service.InterFace;


import com.example.report_service.DTO.Response.ApplicationResponse;
import com.example.report_service.DTO.Response.ResponseApi;
import com.example.report_service.Service.CandidateServiceFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service", url = "${feign.client.user-service.url}",fallback= CandidateServiceFallback.class)
public interface CandidateClient {
    @GetMapping("/api/application/get_by_id/{id}")
    ResponseEntity<ResponseApi<ApplicationResponse>> getApplicationById(@PathVariable("id") String id);
}
