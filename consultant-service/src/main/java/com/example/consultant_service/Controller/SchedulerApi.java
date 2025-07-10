package com.example.consultant_service.Controller;

import com.example.consultant_service.Entity.Scheduler;
import com.example.consultant_service.Model.Request.Booking1Request;
import com.example.consultant_service.Model.Request.CreateSchedulerRequest;
import com.example.consultant_service.Model.Request.FilterSchedulerRequest;
import com.example.consultant_service.Model.Request.SchedulerResponse;
import com.example.consultant_service.Model.Response.BookingResponse;
import com.example.consultant_service.Model.Response.DataResponse;
import com.example.consultant_service.Service.SchedulerService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("api/scheduler")
@SecurityRequirement(name = "api")
public class SchedulerApi {
    @Autowired
    private SchedulerService schedulerService;

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<Scheduler> create (@RequestBody CreateSchedulerRequest createSchedulerRequest){
        Scheduler scheduler = schedulerService.create(createSchedulerRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(scheduler);
    }

    @GetMapping("/filter")
    public ResponseEntity<DataResponse<BookingResponse>> filter
            (@ModelAttribute FilterSchedulerRequest filterSchedulerRequest){
        DataResponse<BookingResponse> scheduler = schedulerService.filter(filterSchedulerRequest);
        return ResponseEntity.ok(scheduler);
    }
    @GetMapping("/get")
    public ResponseEntity<DataResponse<SchedulerResponse>> getAll(@RequestParam int page, @RequestParam int size){
        DataResponse<SchedulerResponse> scheduler = schedulerService.getAll(page,size);
        return ResponseEntity.ok(scheduler);
    }
    @PreAuthorize("hasAuthority('CONSULTANT')")
    @GetMapping("/staff/{staffUuid}")
    public ResponseEntity<List<SchedulerResponse>> getByStaff(@PathVariable("staffUuid") String uuid){
        List<SchedulerResponse> schedulers = schedulerService.getByStaff(uuid);
        return ResponseEntity.ok(schedulers);
    }
    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<Scheduler> update (@PathVariable("id") String uuid, @RequestBody CreateSchedulerRequest createSchedulerRequest){
        Scheduler scheduler = schedulerService.update(uuid,createSchedulerRequest);
        return ResponseEntity.ok(scheduler);
    }
    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Scheduler> delete(@PathVariable("id") String uuid){
        Scheduler scheduler = schedulerService.delete(uuid);
        return ResponseEntity.ok(scheduler);
    }
}
