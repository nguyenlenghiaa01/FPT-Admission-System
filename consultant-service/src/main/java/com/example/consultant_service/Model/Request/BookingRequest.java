package com.example.consultant_service.Model.Request;

import com.example.consultant_service.Entity.Scheduler;
import com.example.consultant_service.Enum.StatusEnum;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BookingRequest {

    private String userUuid;

    private Scheduler scheduler;

    @Enumerated(EnumType.STRING)
    private StatusEnum status;
}
