package com.example.consultant_service.Controller;

import com.example.consultant_service.Entity.Scheduler;
import com.example.consultant_service.Model.Request.CreateSchedulerRequest;
import com.example.consultant_service.Model.Response.DataResponse;
import com.example.consultant_service.Service.SchedulerService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api")
@SecurityRequirement(name = "api")
@CrossOrigin("*")
public class SchedulerApi {
    @Autowired
    private SchedulerService schedulerService;

    @PostMapping("/scheduler/create")
    public ResponseEntity<Scheduler> create (@RequestBody CreateSchedulerRequest createSchedulerRequest){
        Scheduler scheduler = schedulerService.create(createSchedulerRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(scheduler);

    }
    @GetMapping("/scheduler/filter")
    public ResponseEntity<DataResponse<Scheduler>> filter
            (@RequestParam int time, @RequestParam int date,@RequestParam int month,@RequestParam int page, @RequestParam int size){
        DataResponse<Scheduler> scheduler = schedulerService.filter(time,date, month, page, size);
        return ResponseEntity.ok(scheduler);
    }
    @GetMapping("/scheduler/get")
    public ResponseEntity<DataResponse<Scheduler>> getAll(@RequestParam int page, @RequestParam int size){
        DataResponse<Scheduler> scheduler = schedulerService.getAll(page,size);
        return ResponseEntity.ok(scheduler);
    }
    @PutMapping("/scheduler/{id}")
    public ResponseEntity<Scheduler> update (@PathVariable String uuid, @RequestBody CreateSchedulerRequest createSchedulerRequest){
        Scheduler scheduler = schedulerService.update(uuid,createSchedulerRequest);
        return ResponseEntity.ok(scheduler);
    }
    @DeleteMapping("/scheduler/{id}")
    public ResponseEntity<Scheduler> delete(@PathVariable String uuid){
        Scheduler scheduler = schedulerService.delete(uuid);
        return ResponseEntity.ok(scheduler);
    }
}
