package com.fptu.hk7.candidateservice.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ApplicationRequest {
    @NotNull
    private String fullname;

    @Email(message = "Invalid email format")
    private String email;

    @Pattern(regexp = "(84|0[3|5|7|8|9])+(\\d{8})", message = "Invalid phone!")
    private String phone;

    @NotNull
    private LocalDate dob;

    @NotNull
    private String gender;

    @NotNull
    private String province;

    @NotNull
    private String campus;
    @NotNull
    private String specialization;

    private String scholarship;

    private boolean postpaid;


}
