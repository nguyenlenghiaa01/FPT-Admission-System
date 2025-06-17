package com.example.consultant_service.Service;

import com.example.consultant_service.Entity.Booking;
import com.example.consultant_service.Entity.Scheduler;
import com.example.consultant_service.Enum.StatusEnum;
import com.example.consultant_service.Exception.NotFoundException;
import com.example.consultant_service.Model.Request.Booking1Request;
import com.example.consultant_service.Model.Request.CreateSchedulerRequest;
import com.example.consultant_service.Model.Response.DataResponse;
import com.example.consultant_service.Repository.SchedulerRepository;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class SchedulerService {
    @Autowired
    SchedulerRepository schedulerRepository;

    public Scheduler create(CreateSchedulerRequest request) {
        Scheduler scheduler = new Scheduler();
        scheduler.setUuid(UUID.randomUUID().toString());

        List<Booking> bookings = new ArrayList<>();
        for (Booking1Request bReq : request.getBookings()) {
            Booking booking = new Booking();
            booking.setUuid(UUID.randomUUID().toString());
            booking.setStaffUuid(bReq.getStaff_uuid());
            booking.setAvailableDate(bReq.getAvailableDate());
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
            booking.setAvailableDate(bReq.getAvailableDate());
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


    public DataResponse<Scheduler> getAll( int page, int size) {
        Page schedulerPage = schedulerRepository.findAll(PageRequest.of(page, size));
        List<Scheduler> schedulers = schedulerPage.getContent();
        List<Scheduler> schedulerResponse = new ArrayList<>();
        for(Scheduler scheduler: schedulers) {
            Scheduler scheduler1 = new Scheduler();
            scheduler1.setBookingList(scheduler.getBookingList());

            schedulerResponse.add(scheduler1);
        }

        DataResponse<Scheduler> dataResponse = new DataResponse<Scheduler>();
        dataResponse.setListData(schedulerResponse);
        dataResponse.setTotalElements(schedulerPage.getTotalElements());
        dataResponse.setPageNumber(schedulerPage.getNumber());
        dataResponse.setTotalPages(schedulerPage.getTotalPages());
        return dataResponse;
    }

    public DataResponse<Scheduler> filter(int time, int date, int month, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        Specification<Scheduler> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (time != 0) {
                Expression<Integer> hourExpression = cb.function("HOUR", Integer.class, root.get("availableDate"));
                predicates.add(cb.equal(hourExpression, time));
            }

            if (date != 0) {
                Expression<Integer> dayExpression = cb.function("DAY", Integer.class, root.get("availableDate"));
                predicates.add(cb.equal(dayExpression, date));
            }

            if (month != 0) {
                Expression<Integer> monthExpression = cb.function("MONTH", Integer.class, root.get("availableDate"));
                predicates.add(cb.equal(monthExpression, month));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        Page<Scheduler> schedulerPage = schedulerRepository.findAll(spec, pageable);

        // Chuẩn bị dữ liệu trả về
        List<Scheduler> schedulerResponse = new ArrayList<>();
        for (Scheduler scheduler : schedulerPage.getContent()) {
            Scheduler scheduler1 = new Scheduler();
            scheduler1.setBookingList(scheduler.getBookingList());
            schedulerResponse.add(scheduler1);
        }

        DataResponse<Scheduler> dataResponse = new DataResponse<>();
        dataResponse.setListData(schedulerResponse);
        dataResponse.setTotalElements(schedulerPage.getTotalElements());
        dataResponse.setPageNumber(schedulerPage.getNumber());
        dataResponse.setTotalPages(schedulerPage.getTotalPages());

        return dataResponse;
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
