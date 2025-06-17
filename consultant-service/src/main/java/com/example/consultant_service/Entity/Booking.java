package com.example.consultant_service.Entity;

import com.example.consultant_service.Enum.StatusEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "booking")
public class Booking {

    @Id
    private String uuid;

    @Column(name = "user_uuid")
    private String candidateUuid;

    @Column(name = "staff_uuid")
    private String staffUuid;

    @ManyToOne
    @JoinColumn(name = "schedule_uuid")
    private Scheduler scheduler;

    @Column(name = "create_at")
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    private StatusEnum status;

    @Column(name = "available_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yy")
    private LocalDateTime availableDate;

    @Column(name = "start_time")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yy HH:mm")
    private LocalDateTime startTime;

    @Column(name = "end_time")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yy HH:mm")
    private LocalDateTime endTime;
}
