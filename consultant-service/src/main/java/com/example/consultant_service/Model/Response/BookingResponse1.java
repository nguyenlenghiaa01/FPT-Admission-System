package com.example.consultant_service.Model.Response;

import com.example.consultant_service.Entity.Scheduler;
import com.example.consultant_service.Enum.StatusEnum;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BookingResponse1 {
    private String userName;
    private String fullName;
    private String phone;
    private String email;
    private String address;
    private String image;
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
