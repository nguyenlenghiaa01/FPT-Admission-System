package com.fptu.hk7.candidateservice.InterFace;

import com.fptu.hk7.candidateservice.event.BookingEvent;
import com.fptu.hk7.candidateservice.event.ReturnApplication;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "consultant-service", url = "${feign.client.consultant-service.url}")
public interface BookingConsultantClient {
    @PostMapping(value = "/api/booking/book")
    ResponseEntity<ReturnApplication> bookingConsultant(@RequestBody BookingEvent bookingEvent);
}
