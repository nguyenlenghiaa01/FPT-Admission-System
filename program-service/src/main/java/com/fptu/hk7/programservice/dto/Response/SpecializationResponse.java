package com.fptu.hk7.programservice.dto.Response;

import com.fptu.hk7.programservice.pojo.Major;
import lombok.Data;

import java.util.UUID;

@Data
public class SpecializationResponse {
        private UUID SpecializationId;
        private String name;
        private String description;
        private Major major;
}
