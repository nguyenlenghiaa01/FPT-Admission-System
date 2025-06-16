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
    @Column(name = "staff_uuid")
    private String staffUUID;

    @OneToMany(mappedBy = "scheduler", cascade = CascadeType.ALL)
    private List<Booking> bookingList;
}
