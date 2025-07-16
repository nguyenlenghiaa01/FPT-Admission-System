package com.example.report_service.DTO.Response;

import com.example.report_service.Enum.ApplicationStatus;
import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
public class ApplicationResponse {
    private UUID id;
    private UUID offering_id;
    private ApplicationStatus status;
    private Date createAt;
    private Date updateAt;
    private String message;
}
