package com.example.consultant_service.Model.Request;

import com.example.consultant_service.Entity.Booking;
import lombok.Data;

import java.util.List;

@Data
public class SchedulerResponse {
    private String uuid;
    private List<Booking> bookingList;
}
