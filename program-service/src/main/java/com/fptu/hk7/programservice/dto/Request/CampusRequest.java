package com.fptu.hk7.programservice.dto.Request;

import lombok.Data;

import java.util.UUID;

@Data
public class CampusRequest {

    private String CampusName;

    private String address;

    private String description;

}
