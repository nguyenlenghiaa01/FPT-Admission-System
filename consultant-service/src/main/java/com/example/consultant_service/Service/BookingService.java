package com.example.consultant_service.Service;

import com.example.consultant_service.Entity.Booking;
import com.example.consultant_service.Enum.StatusEnum;
import com.example.consultant_service.Exception.NotFoundException;
import com.example.consultant_service.InterFace.IBookingService;
import com.example.consultant_service.Model.Request.BookingRequest;
import com.example.consultant_service.Model.Request.BookingUpdateRequest;
import com.example.consultant_service.Model.Request.ReturnApplication;
import com.example.consultant_service.Model.Request.UpdateBookingReq;
import com.example.consultant_service.Model.Response.BookingResponse;
import com.example.consultant_service.Model.Response.DataResponse;
import com.example.consultant_service.Repository.BookingRepository;
import com.example.consultant_service.Service.redis.RedisService;
import com.example.consultant_service.event.BookingReportEvent;
import com.example.consultant_service.event.SocketNewApplicationEvent;
import com.example.consultant_service.event.SubmitApplicationEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BookingService implements IBookingService {

    private final BookingRepository bookingRepository;
    private final ModelMapper modelMapper;
    private final RedisService redisService;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    private final String TOPIC1 = "booking_report";

    public BookingResponse create (BookingRequest bookingRequest){
        Booking booking = new Booking();
        booking.setUuid(UUID.randomUUID().toString());
        booking.setCreatedAt(LocalDateTime.now());
        booking.setScheduler(bookingRequest.getScheduler());
        booking.setStatus(StatusEnum.BOOKED);
        booking.setStaffUuid(null);
        booking.setScheduler(bookingRequest.getScheduler());
        booking.setAvailableDate(booking.getAvailableDate());
        booking.setStartTime(booking.getStartTime());
        booking.setEndTime(bookingRequest.getEndTime());
        booking.setCandidateUuid(bookingRequest.getCandidateUuid());
        Booking newBooking = bookingRepository.save(booking);
        return modelMapper.map(newBooking,BookingResponse.class);
    }

    public DataResponse<BookingResponse> getAll(int page, int size) {
        Page<Booking> bookingPage = bookingRepository.findAll(PageRequest.of(page, size));
        List<Booking> bookings = bookingPage.getContent();
        List<BookingResponse> bookingResponse = new ArrayList<>();
        for(Booking booking: bookings) {
            BookingResponse booking1 = new BookingResponse();
            booking1.setBookingUuid(booking.getUuid());
            booking1.setStatus(booking.getStatus());
            booking1.setScheduler(booking.getScheduler());
            booking1.setBookAt(booking.getCreatedAt());
            booking1.setScheduler(booking.getScheduler());
            booking1.setStatus(booking.getStatus());
            booking1.setStaffUuid(booking.getStaffUuid());
            booking1.setScheduler(booking.getScheduler());
            booking1.setAvailableDate(booking.getAvailableDate());
            booking1.setStartTime(booking.getStartTime());
            booking1.setEndTime(booking.getEndTime());
            booking1.setCandidateUuid(booking.getCandidateUuid());

            bookingResponse.add(booking1);
        }

        DataResponse<BookingResponse> dataResponse = new DataResponse<>();
        dataResponse.setListData(bookingResponse);
        dataResponse.setTotalElements(bookingPage.getTotalElements());
        dataResponse.setPageNumber(bookingPage.getNumber());
        dataResponse.setTotalPages(bookingPage.getTotalPages());
        return dataResponse;
    }

    public Booking findBookingByUuid(String uuid){
        Booking booking = bookingRepository.findBookingByUuid(uuid);
        if(booking ==null){
            throw new NotFoundException("Booking not found");
        }
        return booking;
    }

    @Override
    public BookingResponse update(String bookingUuid, UpdateBookingReq bookingRequest) {
        Booking booking = bookingRepository.findBookingByUuid(bookingUuid);
        if(booking == null){
            throw new NotFoundException("Booking not found");
        }
        booking.setStatus(StatusEnum.valueOf(bookingRequest.getStatus()));
        bookingRepository.save(booking);
        return modelMapper.map(booking, BookingResponse.class);
    }

    public BookingResponse update(String bookingUuid,BookingRequest bookingRequest){
        Booking booking = bookingRepository.findBookingByUuid(bookingUuid);
        if(booking == null){
            throw new NotFoundException("Booking not found");
        }
        booking.setScheduler(bookingRequest.getScheduler());
        Booking newBooking = bookingRepository.save(booking);
        return modelMapper.map(newBooking,BookingResponse.class);
    }

    public BookingResponse updateStatus(String uuid, BookingUpdateRequest bookingUpdateRequest){
        Booking booking = bookingRepository.findBookingByUuid(uuid);
        if(booking == null){
           throw new NotFoundException("Booking not found");
        }
        booking.setStatus(bookingUpdateRequest.getStatus());
        booking.setStaffUuid(bookingUpdateRequest.getStaffUuid());
        booking.setCandidateUuid(bookingUpdateRequest.getUserUuid());
        Booking newBooking = bookingRepository.save(booking);
        try{
            BookingReportEvent bookingReportEvent = new BookingReportEvent();
            if(booking.getStatus().equals(StatusEnum.COMPLETED)) {
                bookingReportEvent.setCompletedCount(1);
            }
            if(booking.getStatus().equals(StatusEnum.CANCELED)){
                bookingReportEvent.setCanceled(1);
            }
        }catch(Exception e){
            throw new RuntimeException("Can not publish event to Report-Service" + e.getMessage());
        }
        return modelMapper.map(newBooking,BookingResponse.class);
    }

    public Booking delete (String uuid){
        Booking booking = bookingRepository.findBookingByUuid(uuid);
        if(booking == null){
            throw new NotFoundException("Booking not found");
        }
        bookingRepository.delete(booking);
        return booking;
    }

    public Booking candidateBookingAdmission(Map<String, String> data) throws JsonProcessingException {
        String bookingUuid = data.get("bookingUuid");
        String candidateUuid = data.get("candidateUuid");
        String email = data.get("email");
        String fullname = data.get("fullname");
        String phone = data.get("phone");
        String campus = data.get("campus");
        String specialization = data.get("specialization");

        // update booking, gán candidateUuid
        Booking booking = bookingRepository.findBookingByUuid(bookingUuid);

        // check status của booking
        String applicationStatus = "APPROVED";
        String note = "Booking đã được xác nhận";
        if(booking == null) throw new NotFoundException("Booking not found");
        if(booking.getStatus() == StatusEnum.BOOKED || StringUtils.hasText(booking.getCandidateUuid())){
            applicationStatus = "REJECTED";
            note = "Booking đã được đăng kí bởi người khác";
            throw new NotFoundException("Booking not available");
        }

        // cập nhật thông tin application
        ReturnApplication returnApplication = ReturnApplication.builder()
                .booking_id(bookingUuid)
                .status(applicationStatus)
                .note(note)
                .build();
        kafkaTemplate.send("return_application_submit", objectMapper.writeValueAsString(returnApplication));

        // lưu booking với candidateUuid
        booking.setCandidateUuid(candidateUuid);
        booking.setStatus(StatusEnum.BOOKED);
        bookingRepository.save(booking);

        // Gửi sự kiện submit_application - email notification
        try {
            SubmitApplicationEvent event = new SubmitApplicationEvent();
            event.setEmail(email);
            event.setFullname(fullname);
            event.setPhone(phone);
            event.setCampus(campus);
            event.setSpecialization(specialization);

            // notification-service
            String TOPIC = "submit_application";
            kafkaTemplate.send(TOPIC, objectMapper.writeValueAsString(event));
            // publish event to new-application
            SocketNewApplicationEvent socketEvent = new SocketNewApplicationEvent();
            socketEvent.setBookingUuid(bookingUuid);
            socketEvent.setConsultantUuid(booking.getStaffUuid());
            socketEvent.setSubmitApplicationEvent(event);
            redisService.sendApplicationMessage(socketEvent, "new-application");
        } catch (Exception e) {
            throw new RuntimeException("Không thể tạo kafka-event submit_application: " + e.getMessage());
        }
        return booking;
    }

    public DataResponse<BookingResponse> getByStaff(String staffUuid, int page, int size) {
        Page<Booking> bookings = bookingRepository.findBookingByStaffUuid(staffUuid, PageRequest.of(page, size));
        if(bookings.isEmpty()) throw new NotFoundException("Booking not found");

        List<BookingResponse> bookingResponses = bookings.getContent().stream()
                .map(booking -> modelMapper.map(booking, BookingResponse.class))
                .toList();

        DataResponse<BookingResponse> dataResponse = new DataResponse<>();
        dataResponse.setListData(bookingResponses);
        dataResponse.setTotalElements(bookings.getTotalElements());
        dataResponse.setPageNumber(bookings.getNumber());
        dataResponse.setTotalPages(bookings.getTotalPages());
        return dataResponse;
    }

    public List<BookingResponse> getByUser(String userUuid){
        List<Booking> bookings = bookingRepository.findBookingByCandidateUuid(userUuid);
        if(bookings == null){
            throw new NotFoundException("Not found booking");
        }
        List<BookingResponse> bookingResponses = new ArrayList<>();
        for(Booking booking : bookings){
            BookingResponse bookingResponse = new BookingResponse();
            bookingResponse.setStaffUuid(booking.getStaffUuid());
            bookingResponse.setCandidateUuid(booking.getCandidateUuid());
            bookingResponse.setBookingUuid(booking.getUuid());
            bookingResponse.setStatus(booking.getStatus());
            bookingResponse.setScheduler(booking.getScheduler());
            bookingResponse.setBookAt(booking.getCreatedAt());
            bookingResponse.setStartTime(booking.getStartTime());
            bookingResponse.setEndTime(booking.getEndTime());

            bookingResponses.add(bookingResponse);
        }
        return bookingResponses;
    }
}
