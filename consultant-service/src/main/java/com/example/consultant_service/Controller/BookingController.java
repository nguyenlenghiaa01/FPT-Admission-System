package com.example.consultant_service.Controller;


import com.example.consultant_service.Entity.Booking;
import com.example.consultant_service.InterFace.IBookingService;
import com.example.consultant_service.Model.Request.BookingRequest;
import com.example.consultant_service.Model.Request.BookingUpdateRequest;
import com.example.consultant_service.Model.Request.UpdateBookingReq;
import com.example.consultant_service.Model.Response.BookingResponse;
import com.example.consultant_service.Model.Response.DataResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/booking")
@SecurityRequirement(name = "api")
@RequiredArgsConstructor

public class BookingController {

    private final IBookingService bookingService;

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

    @PreAuthorize("hasAuthority('USER')")
    @PutMapping("/{id}")
    public ResponseEntity<BookingResponse> update(@PathVariable("id") String uuid, @RequestBody UpdateBookingReq bookingRequest) {
        BookingResponse bookingResponse = bookingService.update(uuid, bookingRequest);
        return ResponseEntity.ok(bookingResponse);
    }

    @PreAuthorize("hasAuthority('CONSULTANT')")
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

    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("/getByUser/{id}")
    public ResponseEntity<List<BookingResponse>> getByUser(@PathVariable("id")String userUuid){
        return ResponseEntity.ok(bookingService.getByUser(userUuid));
    }
}
