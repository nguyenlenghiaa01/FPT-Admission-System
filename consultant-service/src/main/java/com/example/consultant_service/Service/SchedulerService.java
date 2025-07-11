package com.example.consultant_service.Service;

import com.example.consultant_service.Controller.UserServiceCaller;
import com.example.consultant_service.Entity.Booking;
import com.example.consultant_service.Entity.Scheduler;
import com.example.consultant_service.Enum.StatusEnum;
import com.example.consultant_service.Exception.NotFoundException;
import com.example.consultant_service.Model.Request.Booking1Request;
import com.example.consultant_service.Model.Request.CreateSchedulerRequest;
import com.example.consultant_service.Model.Request.FilterSchedulerRequest;
import com.example.consultant_service.Model.Request.SchedulerResponse;
import com.example.consultant_service.Model.Response.AccountResponse;
import com.example.consultant_service.Model.Response.BookingResponse;
import com.example.consultant_service.Model.Response.BookingResponse1;
import com.example.consultant_service.Model.Response.DataResponse;
import com.example.consultant_service.Repository.BookingRepository;
import com.example.consultant_service.Repository.SchedulerRepository;
import com.example.consultant_service.Service.redis.RedisService;
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
import java.time.temporal.WeekFields;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SchedulerService {
    @Autowired
    SchedulerRepository schedulerRepository;

    @Autowired
    private RedisService redisService;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private UserServiceCaller userServiceCaller;

    public List<Booking> createBookingFromRequest(List<Booking> bookings, List<Booking1Request> booking1Requests, Scheduler scheduler) {
        for (Booking1Request bReq : booking1Requests) {
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
        return bookings;
    }

    @Transactional
    public Scheduler create(CreateSchedulerRequest request) {
        LocalDateTime date = request.getBookings().get(0).getStartTime();
        WeekFields weekFields = WeekFields.ISO;

        int weekOfYear = date.get(weekFields.weekOfWeekBasedYear());
        int year = date.get(weekFields.weekBasedYear());
        int month = date.getMonthValue();

        LocalDate startDate = date.toLocalDate().with(weekFields.dayOfWeek(), 1);

        LocalDate endDate = startDate.plusDays(6);

        // define which scheduler
        Scheduler scheduler = schedulerRepository.findSchedulerByWeekOfYearAndYear(weekOfYear, year).orElse(null);
        // already saved scheduler
        Scheduler savedScheduler;
        try {
            if (scheduler != null) {
                List<Booking> bookings = scheduler.getBookingList();
                createBookingFromRequest(bookings, request.getBookings(), scheduler);
                scheduler.setBookingList(bookings);
                savedScheduler = schedulerRepository.save(scheduler);
            } else {
                Scheduler newScheduler = new Scheduler();
                newScheduler.setUuid(UUID.randomUUID().toString());
                newScheduler.setWeekOfYear(weekOfYear);
                newScheduler.setYear(year);
                newScheduler.setMonth(month);
                newScheduler.setStart_date(startDate);
                newScheduler.setEnd_date(endDate);
                // booking
                List<Booking> bookings = new ArrayList<>();
                createBookingFromRequest(bookings, request.getBookings(), newScheduler);
                newScheduler.setBookingList(bookings);
                savedScheduler = schedulerRepository.save(newScheduler);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error when create or update scheduler: " + e.getMessage());
        }
        // send message to /new-scheduler realtime
        for(Booking1Request bReq : request.getBookings()) {
            redisService.sendApplicationMessage(bReq, "new-scheduler");
        }

        return savedScheduler;
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

    public DataResponse<BookingResponse1> filter(FilterSchedulerRequest filterSchedulerRequest) {
        Pageable pageable = PageRequest.of(filterSchedulerRequest.getPage(), filterSchedulerRequest.getSize());

        Specification<Booking> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filterSchedulerRequest.getTime() != null && !filterSchedulerRequest.getTime().isEmpty()) {
                LocalTime time = LocalTime.parse(filterSchedulerRequest.getTime());

                Expression<LocalDateTime> availableDatePath = root.get("availableDate");

                Expression<Integer> extractedHour = cb.function("date_part", Integer.class,
                        cb.literal("hour"), availableDatePath);
                predicates.add(cb.equal(extractedHour, time.getHour()));
            }

            if (filterSchedulerRequest.getDate() != null && !filterSchedulerRequest.getDate().isEmpty()) {
                LocalDate date = LocalDate.parse(filterSchedulerRequest.getDate());
                Expression<LocalDate> dateExpression = root.get("availableDate").as(LocalDate.class);
                predicates.add(cb.equal(dateExpression, date));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        Page<Booking> bookingPage = bookingRepository.findAll(spec, pageable);
        List<BookingResponse1> bookingResponses = bookingPage.getContent().stream().map(booking -> {
            BookingResponse1 response = new BookingResponse1();
            response.setCandidateUuid(booking.getCandidateUuid());
            response.setStaffUuid(booking.getStaffUuid());
            AccountResponse accountResponse = userServiceCaller.getUserByUuid(booking.getStaffUuid());
            response.setFullName(accountResponse.getFullName());
            response.setImage(accountResponse.getImage());
            response.setEmail(accountResponse.getEmail());
            response.setAddress(accountResponse.getAddress());
            response.setPhone(accountResponse.getPhone());
            response.setAvailableDate(booking.getStartTime());
            response.setStartTime(booking.getStartTime());
            response.setEndTime(booking.getEndTime());
            response.setBookAt(booking.getCreatedAt());
            response.setStatus(booking.getStatus());
            response.setScheduler(booking.getScheduler());
            response.setBookingUuid(booking.getUuid());
            return response;
        }).toList();

        DataResponse<BookingResponse1> dataResponse = new DataResponse<>();
        dataResponse.setListData(bookingResponses);
        dataResponse.setTotalElements(bookingPage.getTotalElements());
        dataResponse.setPageNumber(bookingPage.getNumber());
        dataResponse.setTotalPages(bookingPage.getTotalPages());

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
