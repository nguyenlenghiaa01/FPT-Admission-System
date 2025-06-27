package com.fptu.hk7.candidateservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ScholarshipRequest {
    @NotBlank(message = "scholarship name not blank")
    private String name;
    @NotBlank(message = "scholarship description not blank")
    private String description;
}
