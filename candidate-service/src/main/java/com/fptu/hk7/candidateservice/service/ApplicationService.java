package com.fptu.hk7.candidateservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fptu.hk7.candidateservice.client.OfferingProgramClient;
import com.fptu.hk7.candidateservice.client.OfferingProgramServiceFallback;
import com.fptu.hk7.candidateservice.client.UserClient;
import com.fptu.hk7.candidateservice.client.UserServiceFallback;
import com.fptu.hk7.candidateservice.dto.request.ApplicationRequest;
import com.fptu.hk7.candidateservice.dto.request.FindOfferingRequest;
import com.fptu.hk7.candidateservice.dto.response.AccountResponse;
import com.fptu.hk7.candidateservice.dto.response.ApplicationResponse;
import com.fptu.hk7.candidateservice.dto.response.GetOfferingResponse;
import com.fptu.hk7.candidateservice.dto.response.ResponseApi;
import com.fptu.hk7.candidateservice.enums.ApplicationStatus;
import com.fptu.hk7.candidateservice.event.BookingEvent;
import com.fptu.hk7.candidateservice.event.SubmitApplicationEvent;
import com.fptu.hk7.candidateservice.exception.NotFoundException;
import com.fptu.hk7.candidateservice.pojo.Candidate;
import com.fptu.hk7.candidateservice.pojo.StatusApplication;
import com.fptu.hk7.candidateservice.repository.ApplicationRepository;
import com.fptu.hk7.candidateservice.pojo.Application;
import com.fptu.hk7.candidateservice.service.redis.RedisService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ApplicationService {
    private final ApplicationRepository applicationRepository;
    private final RedisTemplate<String, Object> redisTemplate;

    ModelMapper modelMapper = new ModelMapper();
    private final UserClient userClient;
    private final OfferingProgramClient offeringProgramClient;
    private final CandidateService candidateService;
    private final ScholarshipService scholarshipService;
    private final StatusApplicationService statusApplicationService;
    private final UserServiceFallback userServiceFallback;
    private final OfferingProgramServiceFallback offeringProgramServiceFallback;
    private final RedisService redisService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public Application createApplication(Application application) {
        return applicationRepository.save(application);
    }

    public Application getApplicationById(UUID id) {
        return applicationRepository.findById(id).orElseThrow(() -> new NotFoundException("Application not found with id: " + id));
    }

    public List<Application> getAllApplications() {
        return applicationRepository.findAll();
    }

    public List<Application> getApplicationsByCandidate(Candidate candidate) {
        return applicationRepository.findAllByCandidate(candidate);
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

    private final String TOPIC2 = "booking_admission"; // consultant-service

    public ResponseEntity<ResponseApi<ApplicationResponse>> submitApplication(ApplicationRequest applicationRequest){
        Candidate candidate = modelMapper.map(applicationRequest, Candidate.class);

        String candidateUuid = Optional.ofNullable(userServiceFallback.getAccountByEmail(candidateService.getCurrentEmailUser()))
                .map(AccountResponse::getUuid)
                .orElseThrow(() -> new NotFoundException(
                        "Cannot find account with email: "
                                + candidateService.getCurrentEmailUser()
                                + " in database. Please register account first!"
                ));
        candidate.setId(UUID.fromString(candidateUuid));
        candidateService.createCandidate(candidate);

        GetOfferingResponse offering = offeringProgramServiceFallback.getOffering(
                new FindOfferingRequest(applicationRequest.getSpecializationUuid(), applicationRequest.getCampusUuid())
        ).getBody();

        // Tạo và lưu Application trước
        Application application = new Application();
        assert offering != null;
        application.setOffering_id(offering.getOfferingId());
        application.setCandidate(candidate);
        application.setBooking_id(UUID.fromString(applicationRequest.getBookingUuid()));
        application.setScholarship(scholarshipService.getScholarshipById(UUID.fromString(applicationRequest.getScholarshipUuid())));
        createApplication(application); // Lưu trước

        // Sau đó mới tạo và lưu StatusApplication
        StatusApplication statusApplication = new StatusApplication();
        statusApplication.setStatus(ApplicationStatus.PENDING);
        statusApplication.setApplication(application);
        statusApplicationService.create(statusApplication); // Lưu sau

        // Gửi sự kiện Booking tới consultant
        try {
            BookingEvent bookingEvent = new BookingEvent();
            bookingEvent.setBookingUuid(applicationRequest.getBookingUuid());
            bookingEvent.setCandidateUuid(candidateUuid);

            bookingEvent.setEmail(candidate.getEmail());
            bookingEvent.setFullname(candidate.getFullname());
            bookingEvent.setPhone(applicationRequest.getPhone());
            bookingEvent.setCampus(offering.getCampusName());
            bookingEvent.setSpecialization(offering.getSpecializationName());
            kafkaTemplate.send(TOPIC2, objectMapper.writeValueAsString(bookingEvent));
        } catch (Exception e) {
            throw new RuntimeException("Không thể tạo kafka-event booking: " + e.getMessage());
        }

        return ResponseEntity.ok(
                ResponseApi.<ApplicationResponse>builder()
                        .status(200)
                        .message("Send application successfully!")
                        .data(modelMapper.map(application, ApplicationResponse.class))
                        .build()
        );
    }

}
