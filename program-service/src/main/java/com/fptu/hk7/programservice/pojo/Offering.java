package com.fptu.hk7.programservice.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "offering")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Offering {

    @Id
    @GeneratedValue
    private UUID id;

    private int year;
    private int target;
    private long price;

    @ManyToOne
    @JoinColumn(name = "campus_id")
    @JsonIgnore
    private Campus campus;

    @ManyToOne
    @JoinColumn(name = "specialization_id")
    @JsonIgnore
    private Specialization specialization;
}

