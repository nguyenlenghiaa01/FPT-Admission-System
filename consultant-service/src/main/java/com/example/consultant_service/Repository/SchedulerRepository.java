package com.example.consultant_service.Repository;


import com.example.consultant_service.Entity.Booking;
import com.example.consultant_service.Entity.Scheduler;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface SchedulerRepository extends JpaRepository<Scheduler,String> , JpaSpecificationExecutor<Scheduler> {

    Scheduler findSchedulerByUuid(String uuid);

    @Query("SELECT DISTINCT b.scheduler FROM Booking b WHERE b.staffUuid = :staffUuid")
    List<Scheduler> findSchedulersByStaffUuid(@Param("staffUuid") String staffUuid);

    Optional<Scheduler> findSchedulerByWeekOfYearAndYear(int weekOfYear, int year);
}
