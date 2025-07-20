package com.example.consultant_service.Model.Request;

import com.example.consultant_service.Entity.Booking;
import lombok.Data;

import java.util.List;

@Data
public class SchedulerResponse {
    private String uuid;
    private String userName;
    private String fullName;
    private String phone;
    private String email;
    private String address;
    private String image;
    private List<Booking> bookingList;
}
