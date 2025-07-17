package com.fptu.hk7.candidateservice.controller;

import com.fptu.hk7.candidateservice.InterFace.IApplicationService;
import com.fptu.hk7.candidateservice.InterFace.ICandidateService;
import com.fptu.hk7.candidateservice.InterFace.IStatusApplicationService;
import com.fptu.hk7.candidateservice.dto.request.ApplicationRequest;
import com.fptu.hk7.candidateservice.dto.request.UpdateApplicationRequest;
import com.fptu.hk7.candidateservice.dto.response.ApplicationResponse;
import com.fptu.hk7.candidateservice.dto.response.ResponseApi;
import com.fptu.hk7.candidateservice.enums.ApplicationStatus;
import com.fptu.hk7.candidateservice.pojo.Application;
import com.fptu.hk7.candidateservice.pojo.Candidate;
import com.fptu.hk7.candidateservice.pojo.StatusApplication;
import com.fptu.hk7.candidateservice.service.CandidateService;
import com.fptu.hk7.candidateservice.service.StatusApplicationService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/application")
@RequiredArgsConstructor
@SecurityRequirement(name = "api")
public class ApplicationController {

    private final IStatusApplicationService statusApplicationService;
    private final ICandidateService candidateService;
    private final IApplicationService applicationService;

    ModelMapper modelMapper = new ModelMapper();

    @PostMapping("/submit")
    public ResponseEntity<ResponseApi<ApplicationResponse>> submitApplication(@Valid @RequestBody ApplicationRequest applicationRequest) {
        return applicationService.submitApplication(applicationRequest);
    }

    @GetMapping("/get_all_by_user/{candidateId}")
    public ResponseEntity<ResponseApi<List<Application>>> getAllUserApplications(@PathVariable("candidateId") String candidateId) {
        Candidate candidate = candidateService.getCandidateById(UUID.fromString(candidateId));
        List<Application> applications = applicationService.getApplicationsByCandidate(candidate);
        return ResponseEntity.ok(
                ResponseApi.<List<Application>>builder()
                        .isSuccess(true)
                        .status(200)
                        .data(applications)
                        .message("Retrieve all applications of candidate successfully!")
                        .build()
                );
    }

    @GetMapping("/get_by_id/{id}")
    public ResponseEntity<ResponseApi<ApplicationResponse>> getApplicationById(@PathVariable("id") String id) {
        Application application = applicationService.getApplicationById(UUID.fromString(id));
        if (application == null) {
            return ResponseEntity.ok(
                    ResponseApi.<ApplicationResponse>builder()
                            .isSuccess(false)
                            .status(404)
                            .message("Application not found!")
                            .build()
            );
        }
        return ResponseEntity.ok(
                ResponseApi.<ApplicationResponse>builder()
                        .isSuccess(true)
                        .status(200)
                        .data(modelMapper.map(application, ApplicationResponse.class))
                        .message("Retrieve application successfully!")
                        .build()
                );
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
