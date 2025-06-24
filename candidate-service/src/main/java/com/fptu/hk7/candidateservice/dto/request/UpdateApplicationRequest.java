package com.fptu.hk7.candidateservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.UUID;

@Data
public class UpdateApplicationRequest {
    @NotBlank(message = "UUID not be blank")
    private UUID id;
    @NotBlank(message = "UUID not be blank")
    private String status;
    private String note;
}
