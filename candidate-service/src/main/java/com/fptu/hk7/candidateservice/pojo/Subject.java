package com.fptu.hk7.candidateservice.pojo;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

import java.util.UUID;

@Entity
@Table(name = "subject")
@Data
public class Subject {

    @Id
    @GeneratedValue
    private UUID id;

    @DecimalMin(value = "0.0", inclusive = true, message = "Mark must be >= 0")
    @DecimalMax(value = "10.0", inclusive = true, message = "Mark must be <= 10")
    private float mark;

    @Min(value = 11, message = "Grade must be 11 or 12")
    @Max(value = 12, message = "Grade must be 11 or 12")
    private int grade;

    @ManyToOne
    @JoinColumn(name = "academic_record_id")
    private AcademicRecord academicRecord;

}
