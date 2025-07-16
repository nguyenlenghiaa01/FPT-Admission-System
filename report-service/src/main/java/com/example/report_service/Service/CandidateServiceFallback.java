package com.example.report_service.Service;

import com.example.report_service.DTO.Response.ApplicationResponse;
import com.example.report_service.DTO.Response.ResponseApi;
import com.example.report_service.InterFace.CandidateClient;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

public class CandidateServiceFallback {

    @Autowired
    private CandidateClient candidateClient;

    @CircuitBreaker(name = "getApplicationCB", fallbackMethod = "getApplicationFallback")
    public ResponseEntity<ResponseApi<ApplicationResponse>> getApplicationById(String uuid) {
        return candidateClient.getApplicationById(uuid);
    }

    public ApplicationResponse getApplicationFallback(String uuid, Throwable t) {
        ApplicationResponse response = new ApplicationResponse();
        response.setMessage("Fallback: user-service unavailable (getEmail)");
        return response;
    }
}
