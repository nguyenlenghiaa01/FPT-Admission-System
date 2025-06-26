package com.fptu.hk7.programservice.dto.Response;

import lombok.Data;

import java.util.UUID;

@Data
public class MajorResponse {
    private UUID MajorId;
    private String name;
    private String description;
}
