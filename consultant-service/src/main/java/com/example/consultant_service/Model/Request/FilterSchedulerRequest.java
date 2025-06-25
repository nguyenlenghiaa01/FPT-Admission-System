package com.example.consultant_service.Model.Request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class FilterSchedulerRequest {

    private String time; // "HH:mm"
    private String date; // "yyyy-MM-dd"
    private int page;
    private int size;
}
