package com.example.consultant_service.Model.Request;
import lombok.Data;

import java.util.List;

@Data
public class CreateSchedulerRequest {
    private List<Booking1Request> bookings;

}
