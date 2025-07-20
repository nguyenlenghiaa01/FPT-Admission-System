package com.example.report_service.Controller;

import com.example.report_service.DTO.Response.BookingReportResponse;
import com.example.report_service.Entity.BookingReport;
import com.example.report_service.InterFace.IBookingReport;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/report")
@RequiredArgsConstructor
@SecurityRequirement(name = "api")
public class BookingReportController {
    private final IBookingReport bookingReport;
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/booking")
    public ResponseEntity<BookingReportResponse> getAll(){
        BookingReportResponse bookingReportResponse = bookingReport.getCount();
        return ResponseEntity.ok(bookingReportResponse);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/filter/booking")
    public ResponseEntity<List<BookingReport>> filter(String campus, Integer month, Integer year){
        List<BookingReport> bookingReports = bookingReport.filter(campus, month, year);
        return ResponseEntity.ok(bookingReports);
    }
}
