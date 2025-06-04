package com.fptu.hk7.candidateservice.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponseApi<T> {
    private String message;
    private T data;
    private int status;
    boolean isSuccess;
}
