package com.fptu.hk7.programservice.dto.Response;

import lombok.Data;

import java.util.List;

@Data
public class DataResponse<T> {
    private List<T> listData;
    private int pageNumber;
    private long totalElements;
    private int totalPages;
}
