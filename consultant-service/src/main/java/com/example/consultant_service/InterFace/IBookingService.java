package com.example.consultant_service.InterFace;

import com.example.consultant_service.Entity.Booking;
import com.example.consultant_service.Model.Request.BookingRequest;
import com.example.consultant_service.Model.Request.BookingUpdateRequest;
import com.example.consultant_service.Model.Request.UpdateBookingReq;
import com.example.consultant_service.Model.Response.BookingResponse;
import com.example.consultant_service.Model.Response.DataResponse;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;
import java.util.Map;

public interface IBookingService {
    BookingResponse create(BookingRequest bookingRequest);

    DataResponse<BookingResponse> getAll(int page, int size);

    Booking findBookingByUuid(String uuid);

    BookingResponse update(String bookingUuid, UpdateBookingReq bookingRequest);

    BookingResponse updateStatus(String uuid, BookingUpdateRequest bookingUpdateRequest);

    Booking delete(String uuid);

    Booking candidateBookingAdmission(Map<String, String> data) throws JsonProcessingException;

    DataResponse<BookingResponse> getByStaff(String staffUuid, int page, int size);

    List<BookingResponse> getByUser(String userUuid);

}
