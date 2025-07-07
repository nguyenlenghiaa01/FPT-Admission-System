package com.fptu.hk7.programservice.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "major")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Major {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(unique = true)
    private String name;
    private String description;

    @OneToMany(mappedBy = "major", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Specialization> specializations;

}
