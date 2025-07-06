package com.fptu.hk7.candidateservice.dto.response;

import lombok.Data;

import java.util.UUID;

@Data
public class GetOfferingResponse {
    private UUID offeringId;
    private String specializationName;
    private String campusName;
}
