package com.example.consultant_service.Model.Request;

import com.example.consultant_service.Entity.Booking;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class CreateSchedulerRequest {

    private String userUuid;

    private LocalDateTime availableDate;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

}
