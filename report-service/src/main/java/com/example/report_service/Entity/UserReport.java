package com.example.report_service.Entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "user_statistics")
public class UserReport {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String uuid;

    @Column(name = "week_of_year")
    private Integer weekOfYear;

    @Column(name = "month")
    private Integer month;

    @Column(name = "year")
    private Integer year;

    @Column(name = "new_user")
    private Integer newUser;
}
