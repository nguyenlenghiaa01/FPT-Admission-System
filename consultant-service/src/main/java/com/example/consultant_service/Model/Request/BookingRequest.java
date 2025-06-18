package com.example.consultant_service.Model.Request;

import com.example.consultant_service.Entity.Scheduler;
import com.example.consultant_service.Enum.StatusEnum;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class BookingRequest {

    private String candidateUuid;

    private Scheduler scheduler;

    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    private StatusEnum status;

    private LocalDate availableDate;

    private LocalDateTime startTime;

    private LocalDateTime endTime;
}
