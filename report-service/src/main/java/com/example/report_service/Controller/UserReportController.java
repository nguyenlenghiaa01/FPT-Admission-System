package com.example.report_service.Controller;

import com.example.report_service.DTO.Response.UserReportResponse;
import com.example.report_service.Entity.BookingReport;
import com.example.report_service.Entity.UserReport;
import com.example.report_service.InterFace.IUserReport;
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
public class UserReportController {

    private final IUserReport iUserReport;

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/user")
    public ResponseEntity<UserReportResponse> getAll(){
        UserReportResponse userReportResponse = iUserReport.getCount();
        return ResponseEntity.ok(userReportResponse);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/filter/user")
    public ResponseEntity<List<UserReport>> filter(Integer weekOfYear, Integer month, Integer year){
        List<UserReport> userReports = iUserReport.filter(weekOfYear, month, year);
        return ResponseEntity.ok(userReports);
    }
}
