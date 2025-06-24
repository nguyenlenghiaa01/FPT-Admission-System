package com.example.consultant_service.Service;

import com.example.consultant_service.Entity.Booking;
import com.example.consultant_service.Entity.Scheduler;
import com.example.consultant_service.Enum.StatusEnum;
import com.example.consultant_service.Exception.NotFoundException;
import com.example.consultant_service.Model.Request.BookingRequest;
import com.example.consultant_service.Model.Request.BookingUpdateRequest;
import com.example.consultant_service.Model.Response.BookingResponse;
import com.example.consultant_service.Model.Response.DataResponse;
import com.example.consultant_service.Repository.BookingRepository;
import com.example.consultant_service.Repository.SchedulerRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private SchedulerRepository schedulerRepository;

    @Autowired
    private ModelMapper modelMapper;

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
            booking1.setStatus(booking.getStatus());
            booking1.setScheduler(booking.getScheduler());
            booking1.setBookAt(booking.getCreatedAt());
            booking1.setScheduler(booking.getScheduler());
            booking1.setStatus(StatusEnum.BOOKED);
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

    public Booking candidateBookingAdmission(String schedularUuid, String candidateUuid){
        Scheduler scheduler = schedulerRepository.findSchedulerByUuid(schedularUuid);
        if(scheduler == null) throw new NotFoundException("Scheduler not found");
        Booking booking = bookingRepository.findByScheduler(scheduler);
        if(booking == null) throw new NotFoundException("Booking not found");
        booking.setCandidateUuid(candidateUuid);
        booking.setStatus(StatusEnum.BOOKED);
        return bookingRepository.save(booking);
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
}
