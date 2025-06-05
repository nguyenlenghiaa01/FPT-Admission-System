package com.fptu.hk7.candidateservice.dto.request;

import lombok.Data;

import java.util.UUID;

@Data
public class UpdateApplicationRequest {
    private UUID id;
    private String status;
}
