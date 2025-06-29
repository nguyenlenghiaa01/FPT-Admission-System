package com.fptu.hk7.candidateservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fptu.hk7.candidateservice.client.OfferingProgramClient;
import com.fptu.hk7.candidateservice.client.OfferingProgramServiceFallback;
import com.fptu.hk7.candidateservice.client.UserClient;
import com.fptu.hk7.candidateservice.client.UserServiceFallback;
import com.fptu.hk7.candidateservice.dto.request.ApplicationRequest;
import com.fptu.hk7.candidateservice.dto.request.FindOfferingRequest;
import com.fptu.hk7.candidateservice.dto.response.ApplicationResponse;
import com.fptu.hk7.candidateservice.dto.response.ResponseApi;
import com.fptu.hk7.candidateservice.enums.ApplicationStatus;
import com.fptu.hk7.candidateservice.event.BookingEvent;
import com.fptu.hk7.candidateservice.event.SubmitApplicationEvent;
import com.fptu.hk7.candidateservice.pojo.Candidate;
import com.fptu.hk7.candidateservice.pojo.StatusApplication;
import com.fptu.hk7.candidateservice.repository.ApplicationRepository;
import com.fptu.hk7.candidateservice.pojo.Application;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
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
    private final StatusApplicationService statusApplicationService;
    private final UserServiceFallback userServiceFallback;
    private final OfferingProgramServiceFallback offeringProgramServiceFallback;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

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

    private final String TOPIC = "submit_application"; // notification-service

    private final String TOPIC2 = "booking_admission"; // consultant-service

    public ResponseEntity<ResponseApi<ApplicationResponse>> submitApplication(ApplicationRequest applicationRequest){
        Candidate candidate = modelMapper.map(applicationRequest, Candidate.class);

        UUID id = UUID.fromString(userServiceFallback.getAccountByEmail(candidateService.getCurrentEmailUser()).getUuid());
        candidate.setId(id);
        candidateService.createCandidate(candidate);

        UUID offering_id = offeringProgramServiceFallback.getOffering(
                new FindOfferingRequest(applicationRequest.getSpecializationUuid(), applicationRequest.getCampusUuid())
        ).getBody();

        // ✅ Tạo và lưu Application trước
        Application application = new Application();
        application.setOffering_id(offering_id);
        application.setCandidate(candidate);
        application.setScholarship(scholarshipService.getScholarshipById(UUID.fromString(applicationRequest.getScholarshipUuid())));
        createApplication(application); // Lưu trước

        // ✅ Sau đó mới tạo và lưu StatusApplication
        StatusApplication statusApplication = new StatusApplication();
        statusApplication.setStatus(ApplicationStatus.PENDING);
        statusApplication.setApplication(application);
        statusApplicationService.create(statusApplication); // Lưu sau

        // Gửi sự kiện Booking
        try {
            BookingEvent bookingEvent = new BookingEvent();
            bookingEvent.setSchedularUuid(applicationRequest.getSchedularUuid());
            bookingEvent.setCandidateUuid(String.valueOf(id));
            kafkaTemplate.send(TOPIC2, objectMapper.writeValueAsString(bookingEvent));
        } catch (Exception e) {
            throw new RuntimeException("Không thể tạo kafka-event booking: " + e.getMessage());
        }

        // Gửi sự kiện submit_application
        try {
            SubmitApplicationEvent event = new SubmitApplicationEvent();
            event.setEmail(candidate.getEmail());
            event.setFullname(candidate.getFullname());
            event.setPhone(applicationRequest.getPhone());
            event.setCampus(applicationRequest.getCampusUuid());
            event.setSpecialization(applicationRequest.getSpecializationUuid());

            kafkaTemplate.send(TOPIC, objectMapper.writeValueAsString(event));
        } catch (Exception e) {
            throw new RuntimeException("Không thể tạo kafka-event submit_application: " + e.getMessage());
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
