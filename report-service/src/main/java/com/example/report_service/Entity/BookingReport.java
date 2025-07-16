package com.example.report_service.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table (name="booking_statistics")
public class BookingReport {
    @Id
    @Column(name="booking_uuid")
    private String bookingUuid;

    @Column(name="week_of_year")
    private Integer weekOfYear;

    @Column(name="campus_name")
    private String campusName;

    private Integer month;

    private Integer year;

    @Column(name="staff_uuid")
    private String staffUuid;

    @Column(name ="total_booking")
    private Integer totalBooking;

    @Column(name="completed_count")
    private Integer completedCount;

    @Column(name="pending_count")
    private Integer pendingCount;
}
