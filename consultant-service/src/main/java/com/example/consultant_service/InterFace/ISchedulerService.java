package com.example.consultant_service.InterFace;

import com.example.consultant_service.Entity.Booking;
import com.example.consultant_service.Entity.Scheduler;
import com.example.consultant_service.Model.Request.Booking1Request;
import com.example.consultant_service.Model.Request.CreateSchedulerRequest;
import com.example.consultant_service.Model.Request.FilterSchedulerRequest;
import com.example.consultant_service.Model.Request.SchedulerResponse;
import com.example.consultant_service.Model.Response.BookingResponse1;
import com.example.consultant_service.Model.Response.DataResponse;

import java.util.List;

public interface ISchedulerService {
    List<Booking> createBookingFromRequest(List<Booking> bookings, List<Booking1Request> booking1Requests, Scheduler scheduler);
    Scheduler create(CreateSchedulerRequest request);
    Scheduler update(String uuid, CreateSchedulerRequest updateSchedulerRequest);
    DataResponse<SchedulerResponse> getAll(int page, int size);
    DataResponse<BookingResponse1> filter(FilterSchedulerRequest filterSchedulerRequest);
    List<SchedulerResponse> getByStaff(String staffUuid);
    Scheduler delete(String uuid);
}
