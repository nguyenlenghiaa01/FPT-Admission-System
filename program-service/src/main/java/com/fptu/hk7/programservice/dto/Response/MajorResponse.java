package com.fptu.hk7.programservice.dto.Response;

import lombok.Data;

import java.util.UUID;

@Data
public class MajorResponse {
    private UUID id;
    private String name;
    private String description;
}
