package com.example.consultant_service.Controller;


import com.example.consultant_service.Entity.Booking;
import com.example.consultant_service.Model.Request.BookingRequest;
import com.example.consultant_service.Model.Request.BookingUpdateRequest;
import com.example.consultant_service.Model.Response.BookingResponse;
import com.example.consultant_service.Model.Response.DataResponse;
import com.example.consultant_service.Service.BookingService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/booking")
@SecurityRequirement(name = "api")
@CrossOrigin("*")
public class BookingApi {

    @Autowired
    private BookingService bookingService;

    @PostMapping("/create")
    public ResponseEntity<BookingResponse> create(@RequestBody BookingRequest bookingRequest) {
        BookingResponse booking = bookingService.create(bookingRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(booking);
    }

    @GetMapping("/get")
    public ResponseEntity<DataResponse<BookingResponse>> getAll
            (@RequestParam int page, @RequestParam int size) {
        DataResponse<BookingResponse> bookingResponse = bookingService.getAll(page, size);
        return ResponseEntity.ok(bookingResponse);
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<Booking> getByUuid(@PathVariable("uuid") String uuid) {
        Booking booking = bookingService.findBookingByUuid(uuid);
        return ResponseEntity.ok(booking);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookingResponse> update(@PathVariable("id") String uuid, @RequestBody BookingRequest bookingRequest) {
        BookingResponse bookingResponse = bookingService.update(uuid, bookingRequest);
        return ResponseEntity.ok(bookingResponse);
    }

    @PutMapping("/status/{id}")
    public ResponseEntity<BookingResponse> update(@PathVariable("id") String uuid, @RequestBody BookingUpdateRequest bookingUpdateRequest) {
        BookingResponse bookingResponse = bookingService.updateStatus(uuid, bookingUpdateRequest);
        return ResponseEntity.ok(bookingResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Booking> delete(@PathVariable("id") String uuid) {
        Booking booking = bookingService.delete(uuid);
        return ResponseEntity.ok(booking);
    }

    @GetMapping("/getByStaff/{id}")
    public ResponseEntity<DataResponse<BookingResponse>> getByStaff(@PathVariable("id") String staffUuid,
                                                                    @RequestParam int page, @RequestParam int size) {
        return ResponseEntity.ok(bookingService.getByStaff(staffUuid, page, size));
    }
}
