package com.fptu.hk7.programservice.dto.Response;

import lombok.Data;

import java.util.UUID;

@Data
public class GetOfferingResponse {
    private UUID offeringId;
    private String specializationName;
    private String campusName;
}
