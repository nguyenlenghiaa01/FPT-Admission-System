package com.fptu.hk7.candidateservice.pojo;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "academic_record")
@Data
public class AcademicRecord {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "top_rank")
    private int topRank;

    @OneToMany(mappedBy = "academicRecord", cascade = CascadeType.ALL)
    private List<Subject> subjects;

    @OneToOne
    @JoinColumn(name = "candidate_id")
    private Candidate candidate;

}
