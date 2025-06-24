package com.example.consultant_service.Repository;

import com.example.consultant_service.Entity.Booking;
import com.example.consultant_service.Entity.Scheduler;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BookingRepository extends JpaRepository<Booking,String> {
    Booking findBookingByUuid(String uuid);

    Booking findByScheduler(Scheduler scheduler);

    Page<Booking> findBookingByStaffUuid(String staffUuid, Pageable pageable);
}
