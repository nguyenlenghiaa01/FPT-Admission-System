package com.fptu.hk7.candidateservice.client;

import com.fptu.hk7.candidateservice.dto.Schedule;
import com.fptu.hk7.candidateservice.dto.response.ScheduleResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "schedule-service", url = "${feign.client.schedule.service.url}")
public interface ScheduleClient {
    @GetMapping("/api/scheduler/get")
    ScheduleResponse<Schedule> getSchedule();
}
