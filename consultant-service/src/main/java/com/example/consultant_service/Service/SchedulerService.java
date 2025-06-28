package com.example.consultant_service.Service;

import com.example.consultant_service.Entity.Booking;
import com.example.consultant_service.Entity.Scheduler;
import com.example.consultant_service.Enum.StatusEnum;
import com.example.consultant_service.Exception.NotFoundException;
import com.example.consultant_service.Model.Request.Booking1Request;
import com.example.consultant_service.Model.Request.CreateSchedulerRequest;
import com.example.consultant_service.Model.Request.FilterSchedulerRequest;
import com.example.consultant_service.Model.Request.SchedulerResponse;
import com.example.consultant_service.Model.Response.BookingResponse;
import com.example.consultant_service.Model.Response.DataResponse;
import com.example.consultant_service.Repository.SchedulerRepository;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class SchedulerService {
    @Autowired
    SchedulerRepository schedulerRepository;

    @Transactional
    public Scheduler create(CreateSchedulerRequest request) {
        Scheduler scheduler = new Scheduler();
        scheduler.setUuid(UUID.randomUUID().toString());

        List<Booking> bookings = new ArrayList<>();
        for (Booking1Request bReq : request.getBookings()) {
            Booking booking = new Booking();
            booking.setUuid(UUID.randomUUID().toString());
            booking.setStaffUuid(bReq.getStaff_uuid());
            booking.setAvailableDate(bReq.getStartTime());
            booking.setStartTime(bReq.getStartTime());
            booking.setEndTime(bReq.getEndTime());
            booking.setCreatedAt(LocalDateTime.now());
            booking.setStatus(StatusEnum.AVAILABLE);
            booking.setScheduler(scheduler);

            bookings.add(booking);
        }

        scheduler.setBookingList(bookings);

        return schedulerRepository.save(scheduler);
    }


    public Scheduler update(String uuid, CreateSchedulerRequest updateSchedulerRequest) {
        Scheduler scheduler = schedulerRepository.findSchedulerByUuid(uuid);
        if (scheduler == null) {
            throw new NotFoundException("Not found scheduler");
        }

        List<Booking> updatedBookings = new ArrayList<>();
        for (Booking1Request bReq : updateSchedulerRequest.getBookings()) {
            Booking booking = new Booking();
            booking.setStaffUuid(bReq.getStaff_uuid());
            booking.setAvailableDate(bReq.getStartTime());
            booking.setStartTime(bReq.getStartTime());
            booking.setEndTime(bReq.getEndTime());
            booking.setCreatedAt(LocalDateTime.now());
            booking.setStatus(StatusEnum.AVAILABLE);
            booking.setScheduler(scheduler);

            updatedBookings.add(booking);
        }

        scheduler.setBookingList(updatedBookings);
        return schedulerRepository.save(scheduler);
    }


    public DataResponse<SchedulerResponse> getAll(int page, int size) {
        Page<Scheduler> schedulerPage = schedulerRepository.findAll(PageRequest.of(page, size));

        List<SchedulerResponse> schedulerResponse = schedulerPage.getContent()
                .stream()
                .map(s -> {
                    SchedulerResponse dto = new SchedulerResponse();
                    dto.setUuid(s.getUuid());
                    dto.setBookingList(s.getBookingList());
                    return dto;
                })
                .collect(Collectors.toList());

        DataResponse<SchedulerResponse> dataResponse = new DataResponse<>();
        dataResponse.setListData(schedulerResponse);
        dataResponse.setTotalElements(schedulerPage.getTotalElements());
        dataResponse.setPageNumber(schedulerPage.getNumber());
        dataResponse.setTotalPages(schedulerPage.getTotalPages());

        return dataResponse;
    }

    public DataResponse<BookingResponse> filter(FilterSchedulerRequest filterSchedulerRequest) {
        Pageable pageable = PageRequest.of(filterSchedulerRequest.getPage(), filterSchedulerRequest.getSize());

        Specification<Scheduler> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            Join<Scheduler, Booking> bookingJoin = root.join("bookingList", JoinType.INNER);

            if (filterSchedulerRequest.getTime() != null && !filterSchedulerRequest.getTime().isEmpty()) {
                LocalTime time = LocalTime.parse(filterSchedulerRequest.getTime());
                Expression<LocalDateTime> availableDatePath = bookingJoin.get("availableDate");

                Expression<Integer> extractedHour = cb.function("date_part", Integer.class,
                        cb.literal("hour"), availableDatePath);
                predicates.add(cb.equal(extractedHour, time.getHour()));
            }

            if (filterSchedulerRequest.getDate() != null && !filterSchedulerRequest.getDate().isEmpty()) {
                LocalDate date = LocalDate.parse(filterSchedulerRequest.getDate());
                Expression<LocalDate> dateExpression = bookingJoin.get("availableDate").as(LocalDate.class);
                predicates.add(cb.equal(dateExpression, date));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        Page<Scheduler> schedulerPage = schedulerRepository.findAll(spec, pageable);
        List<Scheduler> schedulerResponse = schedulerPage.getContent();
        List<BookingResponse> bookings = new ArrayList<>();
        for (Scheduler bReq : schedulerResponse) {
            for (Booking abc : bReq.getBookingList()) {
                BookingResponse booking = new BookingResponse();
                booking.setCandidateUuid(UUID.randomUUID().toString());
                booking.setStaffUuid(abc.getStaffUuid());
                booking.setAvailableDate(abc.getStartTime());
                booking.setStartTime(abc.getStartTime());
                booking.setEndTime(abc.getEndTime());
                booking.setBookAt(abc.getCreatedAt());
                booking.setStatus(abc.getStatus());
                booking.setScheduler(abc.getScheduler());

                bookings.add(booking);
            }
        }

            DataResponse<BookingResponse> dataResponse = new DataResponse<>();
            dataResponse.setListData(bookings);
            dataResponse.setTotalElements(schedulerPage.getTotalElements());
            dataResponse.setPageNumber(schedulerPage.getNumber());
            dataResponse.setTotalPages(schedulerPage.getTotalPages());

            return dataResponse;
    }
    public List<SchedulerResponse> getByStaff(String staffUuid) {
        List<Scheduler> schedulers = schedulerRepository.findSchedulersByStaffUuid(staffUuid);
        List<SchedulerResponse> responseList = new ArrayList<>();

        for (Scheduler scheduler : schedulers) {
            SchedulerResponse response = new SchedulerResponse();
            response.setUuid(scheduler.getUuid());

            // Tạo danh sách Booking phù hợp
            List<Booking> bookingList = new ArrayList<>();
            if (scheduler.getBookingList() != null) {
                for (Booking booking : scheduler.getBookingList()) {
                    if (staffUuid.equals(booking.getStaffUuid())) {
                        bookingList.add(booking);
                    }
                }
            }

            response.setBookingList(bookingList);
            responseList.add(response);
        }

        return responseList;
    }


    public Scheduler delete(String uuid){
        Scheduler scheduler = schedulerRepository.findSchedulerByUuid(uuid);
        if(scheduler == null){
            throw new NotFoundException("Not foud Scheduler");
        }
        schedulerRepository.delete(scheduler);
        return scheduler;
    }
}
