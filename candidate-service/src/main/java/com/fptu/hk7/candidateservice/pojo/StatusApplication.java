package com.fptu.hk7.candidateservice.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fptu.hk7.candidateservice.enums.ApplicationStatus;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "status_application")
@Data
public class StatusApplication {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Enumerated(EnumType.STRING)
    private ApplicationStatus status;

    @CreationTimestamp
    @Column(name = "create_at")
    private LocalDateTime createAt;

    @Column(nullable = true)
    private String note;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "application_id")
    private Application application;
}
