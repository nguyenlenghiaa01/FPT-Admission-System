package com.fptu.hk7.candidateservice.controller;

import com.fptu.hk7.candidateservice.dto.request.ApplicationRequest;
import com.fptu.hk7.candidateservice.dto.request.UpdateApplicationRequest;
import com.fptu.hk7.candidateservice.dto.response.ApplicationResponse;
import com.fptu.hk7.candidateservice.dto.response.ResponseApi;
import com.fptu.hk7.candidateservice.enums.ApplicationStatus;
import com.fptu.hk7.candidateservice.pojo.Application;
import com.fptu.hk7.candidateservice.pojo.StatusApplication;
import com.fptu.hk7.candidateservice.service.ApplicationService;
import com.fptu.hk7.candidateservice.service.StatusApplicationService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/application")
@RequiredArgsConstructor
@SecurityRequirement(name = "api")
public class ApplicationController {

    private final ApplicationService applicationService;
    private final StatusApplicationService statusApplicationService;

    ModelMapper modelMapper = new ModelMapper();

    @PostMapping("/submit")
    public ResponseEntity<ResponseApi<ApplicationResponse>> submitApplication(@Valid @RequestBody ApplicationRequest applicationRequest) {
        return applicationService.submitApplication(applicationRequest);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/get_all")
    public ResponseEntity<ResponseApi<List<Application>>> getAllApplications() {
        List<Application> list = applicationService.getAllApplications();
        return ResponseEntity.ok(
                ResponseApi.<List<Application>>builder()
                        .isSuccess(true)
                        .status(200)
                        .data(list)
                        .message("Retrieve all applications successfully!")
                        .build()
                );
    }

    @PreAuthorize("hasAuthority('STAFF')")
    @PutMapping("/update_application")
    public ResponseEntity<ApplicationResponse> updateApplication(@Valid @RequestBody UpdateApplicationRequest updateApplicationRequest) {
        Application application = applicationService.getApplicationById(updateApplicationRequest.getId());
        application.setStatus(ApplicationStatus.valueOf(updateApplicationRequest.getStatus()));

        StatusApplication status = new StatusApplication();
        status.setApplication(application);
        status.setNote(updateApplicationRequest.getNote());
        status.setStatus(ApplicationStatus.valueOf(updateApplicationRequest.getStatus()));
        statusApplicationService.create(status);
        System.out.println("Cập nhật trạng thái đơn thành công");

        return ResponseEntity.ok(
                modelMapper.map(applicationService.updateApplication(updateApplicationRequest.getId(), application), ApplicationResponse.class)
        );
    }
}
