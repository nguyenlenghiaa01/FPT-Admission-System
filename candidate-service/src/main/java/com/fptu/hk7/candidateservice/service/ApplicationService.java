package com.fptu.hk7.candidateservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fptu.hk7.candidateservice.client.OfferingProgramClient;
import com.fptu.hk7.candidateservice.client.UserClient;
import com.fptu.hk7.candidateservice.dto.request.ApplicationRequest;
import com.fptu.hk7.candidateservice.dto.request.FindOfferingRequest;
import com.fptu.hk7.candidateservice.dto.response.ApplicationResponse;
import com.fptu.hk7.candidateservice.dto.response.ResponseApi;
import com.fptu.hk7.candidateservice.event.BookingEvent;
import com.fptu.hk7.candidateservice.event.SubmitApplicationEvent;
import com.fptu.hk7.candidateservice.pojo.Candidate;
import com.fptu.hk7.candidateservice.repository.ApplicationRepository;
import com.fptu.hk7.candidateservice.pojo.Application;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
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

    // Produce Kafka Event
    private final String TOPIC = "submit_application"; // notification-service

    private final String TOPIC2 = "update_application"; // consultant-service

    public ResponseEntity<ResponseApi<ApplicationResponse>> submitApplication(ApplicationRequest applicationRequest){
        Candidate candidate = modelMapper.map(applicationRequest, Candidate.class);

        System.out.println("Start: Lấy thông tin User Account thông qua Feign");
        UUID id = UUID.fromString(userClient.getAccountByEmail(candidateService.getCurrentEmailUser()).getUuid());
        candidate.setId(id);
        System.out.println("UUID của UserAccount: "+id);
        System.out.println("End: Lấy thông tin thành công");
        candidateService.createCandidate(candidate);

        System.out.println("Start: Lấy thông tin Offering UUID");
        UUID offering_id = offeringProgramClient.getOfferingByCampusNameAndSpecializationName(
                new FindOfferingRequest(applicationRequest.getSpecialization(), applicationRequest.getCampus())
        ).getBody();
        System.out.println("UUID Offering lấy được: " + offering_id);
        System.out.println("End: Lấy thông tin thành công");
        Application application = new Application();
        application.setOffering_id(offering_id);
        application.setScholarship(scholarshipService.getScholarshipByName(applicationRequest.getScholarship()));
        createApplication(application);

        System.out.println("Start: Lưu thông tin ứng viên vào Booking bên bảng Booking - Consultant Service");
        try {
            BookingEvent bookingEvent = new BookingEvent();
            bookingEvent.setBookingUuid(applicationRequest.getBookingUuid());

            String jsonBookingEvent = objectMapper.writeValueAsString(bookingEvent);
            System.out.println("Event booking: " + bookingEvent.toString());
            kafkaTemplate.send(TOPIC2, jsonBookingEvent);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        System.out.println("Start: Bắt đầu tạo kafka-event cho submit_application");
        try {
            SubmitApplicationEvent event = new SubmitApplicationEvent();
            event.setEmail(candidate.getEmail());
            event.setFullname(candidate.getFullname());
            event.setPhone(applicationRequest.getPhone());
            event.setCampus(applicationRequest.getCampus());
            event.setSpecialization(applicationRequest.getSpecialization());

            String eventJson = objectMapper.writeValueAsString(event);
            System.out.println("Event submit_application: "+ event.toString());
            kafkaTemplate.send(TOPIC, eventJson);
            System.out.println("Success: Đã tạo ra kafka-event submit_application thành công!");
        } catch (Exception e) {
            System.err.println("X Error: Không thể tạo kafka-event submit_application, " + e.getMessage());
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
