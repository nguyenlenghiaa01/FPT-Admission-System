package com.fptu.hk7.candidateservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fptu.hk7.candidateservice.InterFace.IApplicationService;
import com.fptu.hk7.candidateservice.InterFace.OfferingProgramClient;
import com.fptu.hk7.candidateservice.client.OfferingProgramServiceFallback;
import com.fptu.hk7.candidateservice.InterFace.UserClient;
import com.fptu.hk7.candidateservice.client.UserServiceFallback;
import com.fptu.hk7.candidateservice.dto.request.ApplicationRequest;
import com.fptu.hk7.candidateservice.dto.request.FindOfferingRequest;
import com.fptu.hk7.candidateservice.dto.response.AccountResponse;
import com.fptu.hk7.candidateservice.dto.response.ApplicationResponse;
import com.fptu.hk7.candidateservice.dto.response.GetOfferingResponse;
import com.fptu.hk7.candidateservice.dto.response.ResponseApi;
import com.fptu.hk7.candidateservice.enums.ApplicationStatus;
import com.fptu.hk7.candidateservice.event.ApplicationReportEvent;
import com.fptu.hk7.candidateservice.event.BookingEvent;
import com.fptu.hk7.candidateservice.event.BookingReportEvent;
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

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ApplicationService implements IApplicationService {
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
    private final String TOPIC1 = "booking_report";

    private final String TOPIC2 = "booking_admission";
    private final String TOPIC3 = "application_report";

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

                    try{
                        ApplicationReportEvent applicationReportEvent = new ApplicationReportEvent();
                        applicationReportEvent.setApplicationUuid(application.getId().toString());
                        if(application.getStatus().equals(ApplicationStatus.APPROVED)){
                            applicationReportEvent.setApproved(1);
                        }else {
                            applicationReportEvent.setReject(1);
                        }
                        kafkaTemplate.send(TOPIC3,objectMapper.writeValueAsString(applicationReportEvent));
                    }catch(Exception e){
                        throw new RuntimeException("Can not publish kafka to Report-Service ");
                    }
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
        application.setBookingUuid(UUID.fromString(applicationRequest.getBookingUuid()));
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
            //gui su kien toi report service
            try{
                BookingReportEvent bookingReportEvent = new BookingReportEvent();
                bookingReportEvent.setBookingUuid(applicationRequest.getBookingUuid());
                bookingReportEvent.setCampusName(offering.getCampusName());
                kafkaTemplate.send(TOPIC1,objectMapper.writeValueAsString(bookingReportEvent));
            }catch(Exception e){
                throw new RuntimeException("Can not send kafka to Report-Service"+ e.getMessage());
            }
            // gui su kien toi report service
            try{
                ApplicationReportEvent applicationReportEvent =new ApplicationReportEvent();
                applicationReportEvent.setCampusName(offering.getCampusName());
                applicationReportEvent.setApplicationUuid(application.getId().toString());
                kafkaTemplate.send(TOPIC3,objectMapper.writeValueAsString(applicationReportEvent));
            }catch (Exception e){
                throw new RuntimeException("Can not create kafka-event application: "+ e.getMessage());
            }
        } catch (Exception e) {
            throw new RuntimeException("Can not create kafka-event booking: " + e.getMessage());
        }
        return ResponseEntity.ok(
                ResponseApi.<ApplicationResponse>builder()
                        .status(200)
                        .message("Send application successfully!")
                        .data(modelMapper.map(application, ApplicationResponse.class))
                        .build()
        );
    }

    @Override
    public void returnStatusApplication(Map<String, String> data) {
        String booking_id = data.get("booking_id");
        String status = data.get("status");
        String note = data.get("note");

        Application application = applicationRepository.findApplicationByBookingUuid(UUID.fromString(booking_id))
                .orElseThrow(() -> new NotFoundException("Application not found with booking_id: " + booking_id));

        application.setStatus(ApplicationStatus.valueOf(status));

        StatusApplication statusApplication = new StatusApplication();
        statusApplication.setStatus(ApplicationStatus.valueOf(status));
        statusApplication.setApplication(application);
        statusApplication.setNote(note);
        statusApplication.setCreateAt(LocalDateTime.now());

        statusApplicationService.create(statusApplication);

        updateApplication(application.getId(), application);
        System.out.println("Application status updated successfully: " + application.getId());
    }

}
