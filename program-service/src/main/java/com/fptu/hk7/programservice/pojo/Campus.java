package com.fptu.hk7.programservice.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "campus")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Campus {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(unique = true)
    private String name;

    private String address;

    private String description;

    @Email
    private String email;

    @Pattern(regexp = "(84|0[3|5|7|8|9])+(\\d{8})", message = "Invalid phone!")
    private String phone;

    @JsonIgnore
    @OneToMany(mappedBy = "campus", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Offering> offerings;

}
