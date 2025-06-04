package com.fptu.hk7.candidateservice.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FindOfferingRequest {
    private String specialization;
    private String campus;
}
