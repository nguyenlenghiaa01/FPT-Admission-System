package com.fptu.hk7.programservice.dto.Request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.util.UUID;

@Data
public class CampusRequest {

    @NotBlank(message = "campus name not blank")
    private String CampusName;
    @NotBlank(message = "campus name not blank")
    private String address;
    @NotBlank(message = "campus name not blank")
    private String description;
    @Email(message = "Invalid email")
    private String email;
    @Pattern(regexp = "(84|0[3|5|7|8|9])+(\\d{8})", message = "Invalid phone!")
    private String phone;
    private String imageUrl;

}
