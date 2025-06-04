package com.example.consultant_service.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "scheduler")
public class Scheduler {

    @Id
    private String uuid;

    @Column(name = "user_uuid")
    private String userUuid;

    @Column(name = "available_date")
    private LocalDateTime availableDate;

    @Column(name = "start_time")
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    @OneToMany(mappedBy = "scheduler", cascade = CascadeType.ALL)
    private List<Booking> bookingList;
}
