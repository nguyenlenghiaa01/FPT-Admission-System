package com.example.report_service.Entity;

import com.example.report_service.Enum.UserRole;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "user_statistics")
public class UserReport {

    @Id
    @Column(name = "user_uuid")
    private String userUuid;

    @Column(name = "week_of_year")
    private Integer weekOfYear;

    @Column(name = "month")
    private Integer month;

    @Column(name = "year")
    private Integer year;

    @Column(name = "new_user")
    private Integer newUser;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private UserRole role;
}
