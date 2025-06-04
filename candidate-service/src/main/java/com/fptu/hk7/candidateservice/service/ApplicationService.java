package com.fptu.hk7.candidateservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fptu.hk7.candidateservice.client.OfferingProgramClient;
import com.fptu.hk7.candidateservice.client.UserClient;
import com.fptu.hk7.candidateservice.dto.request.ApplicationRequest;
import com.fptu.hk7.candidateservice.dto.request.FindOfferingRequest;
import com.fptu.hk7.candidateservice.dto.response.ApplicationResponse;
import com.fptu.hk7.candidateservice.dto.response.ResponseApi;
import com.fptu.hk7.candidateservice.event.SubmitApplicationEvent;
import com.fptu.hk7.candidateservice.pojo.Candidate;
import com.fptu.hk7.candidateservice.repository.ApplicationRepository;
import com.fptu.hk7.candidateservice.pojo.Application;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ApplicationService {
    private final ApplicationRepository applicationRepository;

    ModelMapper modelMapper = new ModelMapper();
    private final UserClient userClient;
    private final OfferingProgramClient offeringProgramClient;
    private final CandidateService candidateService;
    private final ScholarshipService scholarshipService;

    public Application createApplication(Application application) {
        return applicationRepository.save(application);
    }

    public Application getApplicationById(UUID id) {
        return applicationRepository.findById(id).orElse(null);
    }

    public List<Application> getAllApplications() {
        return applicationRepository.findAll();
    }

    public Application updateApplication(UUID id, Application updatedApplication) {
        return applicationRepository.findById(id)
                .map(application -> {
                    application.setCandidate(updatedApplication.getCandidate());
                    application.setOffering_id(updatedApplication.getOffering_id());
                    application.setStatus(updatedApplication.getStatus());
                    application.setScholarship(updatedApplication.getScholarship());
                    return applicationRepository.save(application);
                })
                .orElse(null);
    }

    public boolean deleteApplication(UUID id) {
        if (applicationRepository.existsById(id)) {
            applicationRepository.deleteById(id);
            return true;
        }
        return false;
    }

    private final String TOPIC = "submit_application";

    public ResponseEntity<ResponseApi<ApplicationResponse>> submitApplication(ApplicationRequest applicationRequest){
        Candidate candidate = modelMapper.map(applicationRequest, Candidate.class);
        candidate.setId(UUID.fromString(userClient.getAccountByEmail(candidateService.getCurrentEmailUser()).getUuid()));
        candidateService.createCandidate(candidate);
        UUID offering_id = offeringProgramClient.getOfferingByCampusNameAndSpecializationName(
                new FindOfferingRequest(applicationRequest.getSpecialization(), applicationRequest.getCampus())
        ).getBody();
        Application application = new Application();
        application.setOffering_id(offering_id);
        application.setScholarship(scholarshipService.getScholarshipByName(applicationRequest.getScholarship()));
        this.createApplication(application);
        try {
            SubmitApplicationEvent event = new SubmitApplicationEvent();
            event.setEmail(candidate.getEmail());
//            event.setToken();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        return ResponseEntity.ok(
                ResponseApi.<ApplicationResponse>builder()
                        .status(200)
                        .message("Application submitted successfully")
                        .data(modelMapper.map(application, ApplicationResponse.class))
                        .build()
        );
    }
}
