package com.example.consultant_service.Repository;

import com.example.consultant_service.Entity.Booking;
import com.example.consultant_service.Entity.Scheduler;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingRepository extends JpaRepository<Booking,String> {
    Booking findBookingByUuid(String uuid);

    Booking findByScheduler(Scheduler scheduler);
}
