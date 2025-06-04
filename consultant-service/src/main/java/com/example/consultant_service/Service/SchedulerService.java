package com.example.consultant_service.Service;

import com.example.consultant_service.Entity.Scheduler;
import com.example.consultant_service.Exception.NotFoundException;
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
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class SchedulerService {
    @Autowired
    SchedulerRepository schedulerRepository;

    public Scheduler create (CreateSchedulerRequest createSchedulerRequest){
        Scheduler scheduler = new Scheduler();
        scheduler.setUuid(UUID.randomUUID().toString());
        scheduler.setAvailableDate(createSchedulerRequest.getAvailableDate());
        scheduler.setStartTime(createSchedulerRequest.getStartTime());
        scheduler.setEndTime(createSchedulerRequest.getEndTime());
        schedulerRepository.save(scheduler);
        return scheduler;
    }

    public Scheduler update (String uuid,CreateSchedulerRequest updateSchedulerRequest){
        Scheduler scheduler = schedulerRepository.findSchedulerByUuid(uuid);
        if(scheduler == null){
            throw new NotFoundException("Not found scheduler");
        }
        scheduler.setEndTime(updateSchedulerRequest.getEndTime());
        scheduler.setAvailableDate(updateSchedulerRequest.getAvailableDate());
        scheduler.setStartTime(updateSchedulerRequest.getStartTime());
        schedulerRepository.save(scheduler);
        return scheduler;
    }

    public DataResponse<Scheduler> getAll( int page, int size) {
        Page schedulerPage = schedulerRepository.findAll(PageRequest.of(page, size));
        List<Scheduler> schedulers = schedulerPage.getContent();
        List<Scheduler> schedulerResponse = new ArrayList<>();
        for(Scheduler scheduler: schedulers) {
            Scheduler scheduler1 = new Scheduler();
            scheduler1.setUuid(scheduler.getUuid());
            scheduler1.setStartTime(scheduler.getStartTime());
            scheduler1.setAvailableDate(scheduler.getAvailableDate());
            scheduler1.setEndTime(scheduler.getEndTime());
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
            scheduler1.setUuid(scheduler.getUuid());
            scheduler1.setStartTime(scheduler.getStartTime());
            scheduler1.setAvailableDate(scheduler.getAvailableDate());
            scheduler1.setEndTime(scheduler.getEndTime());
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
