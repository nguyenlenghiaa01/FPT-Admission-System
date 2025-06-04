package com.example.consultant_service.Model.Response;

import com.example.consultant_service.Entity.Scheduler;
import com.example.consultant_service.Enum.StatusEnum;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BookingResponse {

    private String UserId;
    private Scheduler scheduler;

    private LocalDateTime bookAt;

    @Enumerated(EnumType.STRING)
    private StatusEnum status;
}
