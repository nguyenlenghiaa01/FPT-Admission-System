package com.example.report_service.Controller;

import com.example.report_service.DTO.Response.UserReportResponse;
import com.example.report_service.InterFace.IUserReport;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/report")
@RequiredArgsConstructor
@SecurityRequirement(name = "api")
public class UserReportController {

    private final IUserReport iUserReport;
    @GetMapping("/user")
    public ResponseEntity<UserReportResponse> getAll(){
        UserReportResponse userReportResponse = iUserReport.getCount();
        return ResponseEntity.ok(userReportResponse);
    }
}
