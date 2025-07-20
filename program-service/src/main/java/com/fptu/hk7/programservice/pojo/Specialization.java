package com.fptu.hk7.programservice.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@Entity(name = "specialization")
@NoArgsConstructor
@AllArgsConstructor
public class Specialization {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(unique = true)
    private String name;

    private String description;

    @ManyToOne
    @JoinColumn(name = "major_id")
    private Major major;

    @JsonIgnore
    @OneToMany(mappedBy = "specialization", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Offering> offerings;

    public Specialization(String name, String description, Major major) {
        this.name = name;
        this.description = description;
        this.major = major;
    }
}
