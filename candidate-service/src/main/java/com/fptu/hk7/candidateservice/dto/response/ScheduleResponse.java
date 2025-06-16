package com.fptu.hk7.candidateservice.dto.response;

import java.util.List;

public class ScheduleResponse<T> {
    private List<T> listData;
    private int pageNumber;
    private long totalElements;
    private int totalPages;
}
