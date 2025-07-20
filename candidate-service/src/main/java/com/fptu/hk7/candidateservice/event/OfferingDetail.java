package com.fptu.hk7.candidateservice.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OfferingDetail {
    private String major;
    private String specialization;
    private String campus;
}
