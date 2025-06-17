package com.example.consultant_service.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "scheduler")
public class Scheduler {

    @Id
    @Column(name = "schedule_uuid")
    private String uuid;

    @OneToMany(mappedBy = "scheduler", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Booking> bookingList;
}
