package com.fptu.hk7.candidateservice.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Schedule {
    private String uuid;

    private String userUuid; // staffUUID

    private LocalDateTime availableDate;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

}
