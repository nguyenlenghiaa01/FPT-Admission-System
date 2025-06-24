package com.fptu.hk7.candidateservice.pojo;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
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

    @Email
    @Column(unique = true)
    private String email;

    @NotBlank(message = "Full name can not be blank")
    private String fullname;

    @NotBlank(message = "Address can not be blank")
    private String address;

    @NotBlank(message = "Province not be blank")
    private String province;

    @CreationTimestamp
    @Column(name = "create_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createAt;

    @OneToMany(mappedBy = "candidate", cascade = CascadeType.ALL)
    private List<Application> applications;

}
