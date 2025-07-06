package com.example.consultant_service.Model.Response;

import com.example.consultant_service.Entity.Scheduler;
import com.example.consultant_service.Enum.StatusEnum;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class BookingResponse {

    private String BookingUuid;

    private String staffUuid;

    private Scheduler scheduler;

    private String CandidateUuid; // user_uuid

    private LocalDateTime bookAt;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private LocalDateTime AvailableDate;

    @Enumerated(EnumType.STRING)
    private StatusEnum status;
}
