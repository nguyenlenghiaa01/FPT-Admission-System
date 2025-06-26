package com.fptu.hk7.programservice.dto.Request;

import com.fptu.hk7.programservice.pojo.Major;
import lombok.Data;

@Data
public class SpecializationRequest {
        private String name;
        private String description;
        private Major major;
}
