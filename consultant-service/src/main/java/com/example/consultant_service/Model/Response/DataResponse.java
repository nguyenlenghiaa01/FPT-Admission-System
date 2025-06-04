package com.example.consultant_service.Model.Response;

import lombok.Data;

import java.util.List;

@Data
public class DataResponse<T> {
    private List<T> listData;
    private int pageNumber;
    private long totalElements;
    private int totalPages;
}
