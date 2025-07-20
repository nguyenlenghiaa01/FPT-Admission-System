package com.example.consultant_service.Model.Request;

import com.example.consultant_service.Entity.Scheduler;
import com.example.consultant_service.Enum.StatusEnum;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

@Data
public class BookingUpdateRequest {
    @Enumerated(EnumType.STRING)
    private StatusEnum status;
}
