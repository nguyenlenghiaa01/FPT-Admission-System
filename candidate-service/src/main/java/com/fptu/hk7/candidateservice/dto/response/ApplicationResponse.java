package com.fptu.hk7.candidateservice.dto.response;


import com.fptu.hk7.candidateservice.enums.ApplicationStatus;
import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
public class ApplicationResponse {
    private UUID id;
    private UUID offering_id;
    private ApplicationStatus status;
    private Date createAt;
    private Date updateAt;
}
