package com.example.consultant_service.Model.Request;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateSchedulerRequest {
    private List<Booking1Request> bookings;
}
