package com.example.consultant_service.Entity;

import com.example.consultant_service.Enum.StatusEnum;
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
    private String userUuid;

    @Column(name="staff_uuid")
    private String staffUuid;

    @ManyToOne
    @JoinColumn(name = "scheduler_id")
    private Scheduler scheduler;

    @Column(name = "create_at")
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    private StatusEnum status;
}
