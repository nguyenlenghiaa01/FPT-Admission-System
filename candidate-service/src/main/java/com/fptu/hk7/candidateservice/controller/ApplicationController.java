package com.fptu.hk7.candidateservice.controller;

import com.fptu.hk7.candidateservice.client.OfferingProgramClient;
import com.fptu.hk7.candidateservice.client.UserClient;
import com.fptu.hk7.candidateservice.dto.request.ApplicationRequest;
import com.fptu.hk7.candidateservice.dto.request.FindOfferingRequest;
import com.fptu.hk7.candidateservice.dto.response.ApplicationResponse;
import com.fptu.hk7.candidateservice.dto.response.ResponseApi;
import com.fptu.hk7.candidateservice.pojo.Application;
import com.fptu.hk7.candidateservice.pojo.Candidate;
import com.fptu.hk7.candidateservice.service.ApplicationService;
import com.fptu.hk7.candidateservice.service.CandidateService;
import com.fptu.hk7.candidateservice.service.ScholarshipService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/application")
@RequiredArgsConstructor
public class ApplicationController {

    private final ApplicationService applicationService;

    @PostMapping("/submit")
    public ResponseEntity<ResponseApi<ApplicationResponse>> submitApplication(@Valid @RequestBody ApplicationRequest applicationRequest) {
        return applicationService.submitApplication(applicationRequest);
    }
}
