package com.example.report_service.Controller;

import com.example.report_service.DTO.Response.ApplicationReportResponse;
import com.example.report_service.Entity.ApplicationReport;
import com.example.report_service.InterFace.IApplicationReport;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/report")
@RequiredArgsConstructor
@SecurityRequirement(name = "api")
public class ApplicationReportController {

    private final IApplicationReport iApplicationReport;

    @GetMapping("/application")
    public ResponseEntity<ApplicationReportResponse> getAll(){
        ApplicationReportResponse applicationReport = iApplicationReport.getCount();
        return ResponseEntity.ok(applicationReport);
    }

    @GetMapping("/filter/application")
    public ResponseEntity<List<ApplicationReport>> filter(String campus, Integer month, Integer year){
        List<ApplicationReport> applicationReports = iApplicationReport.filter(campus, month, year);
        return ResponseEntity.ok(applicationReports);
    }


}
