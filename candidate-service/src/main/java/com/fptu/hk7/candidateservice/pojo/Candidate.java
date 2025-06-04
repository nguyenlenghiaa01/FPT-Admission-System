package com.fptu.hk7.candidateservice.pojo;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.UUID;


@Entity
@Table(name = "candidate")
@Data
public class Candidate {

    @Id
    private UUID id;

    @Column(unique = true)
    private String email;

    private String fullname;

    @Temporal(TemporalType.DATE)
    private LocalDate dob;

    private String gender;

    private String province;

    @CreationTimestamp
    @Column(name = "create_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createAt;

    @OneToMany(mappedBy = "candidate", cascade = CascadeType.ALL)
    private List<Application> applications;

}
