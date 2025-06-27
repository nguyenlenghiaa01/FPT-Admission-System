package com.fptu.hk7.candidateservice.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@Entity(name = "scholarship")
@NoArgsConstructor
@AllArgsConstructor
public class Scholarship {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(unique = true)
    private String name;
    private String description;

    public Scholarship(String name, String description) {
        this.name = name;
        this.description = description;
    }

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "scholarship")
    private List<Application> applications;
}
