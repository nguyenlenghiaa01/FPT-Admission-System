package com.fptu.hk7.candidateservice.client;

import com.fptu.hk7.candidateservice.dto.request.FindOfferingRequest;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.UUID;

@Component
public class OfferingProgramClientFallback implements OfferingProgramClient{
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(UserClientFallback.class);

    @Override
    public ResponseEntity<UUID> getOffering(@RequestBody FindOfferingRequest findOfferingRequest){
        logger.error("Candidate service is unavailable - getOfferingByCampusNameAndSpecializationName fallback");
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(null);
    }
}
