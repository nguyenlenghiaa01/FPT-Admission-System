package com.example.consultant_service.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "scheduler")
public class Scheduler {

    @Id
    @Column(name = "schedule_uuid")
    private String uuid;

    @Column(name = "week_of_year", unique = true)
    private int weekOfYear;

    @Column(name = "month")
    private int month;

    @Column(name = "year")
    private int year;

    private LocalDate start_date;

    private LocalDate end_date;

    @OneToMany(mappedBy = "scheduler", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Booking> bookingList;
}
