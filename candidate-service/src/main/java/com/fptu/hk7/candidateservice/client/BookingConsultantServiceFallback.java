package com.fptu.hk7.candidateservice.client;

import com.fptu.hk7.candidateservice.InterFace.BookingConsultantClient;
import com.fptu.hk7.candidateservice.event.BookingEvent;
import com.fptu.hk7.candidateservice.event.ReturnApplication;
import lombok.RequiredArgsConstructor;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Service
@RequiredArgsConstructor
public class BookingConsultantServiceFallback {
    private final BookingConsultantClient bookingConsultantClient;

    @CircuitBreaker(name = "booking", fallbackMethod = "bookingConsultant")
    public ResponseEntity<ReturnApplication> bookingConsultant(BookingEvent bookingEvent){
        return bookingConsultantClient.bookingConsultant(bookingEvent);
    }

    public ResponseEntity<UUID> bookingConsultantFallback(BookingEvent bookingEvent, Throwable t) {
        System.out.println("Fallback triggered: " + t.getMessage());
        return ResponseEntity.ok(null);
    }
}
