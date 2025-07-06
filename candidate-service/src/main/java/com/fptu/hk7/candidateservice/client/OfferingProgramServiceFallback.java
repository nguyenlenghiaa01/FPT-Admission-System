package com.fptu.hk7.candidateservice.client;

import com.fptu.hk7.candidateservice.dto.request.FindOfferingRequest;
import com.fptu.hk7.candidateservice.dto.response.GetOfferingResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class OfferingProgramServiceFallback{

    @Autowired
    private OfferingProgramClient offeringProgramClient;

    @CircuitBreaker(name = "Offering", fallbackMethod = "getOfferingFallback")
    public ResponseEntity<GetOfferingResponse> getOffering(FindOfferingRequest request) {
        return offeringProgramClient.getOffering(request);
    }

    public ResponseEntity<UUID> getOfferingFallback(FindOfferingRequest request, Throwable t) {
        System.out.println("Fallback triggered: " + t.getMessage());
        return ResponseEntity.ok(null);
    }
}
