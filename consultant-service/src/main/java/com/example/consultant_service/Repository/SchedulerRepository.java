package com.example.consultant_service.Repository;


import com.example.consultant_service.Entity.Scheduler;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;


@Repository
public interface SchedulerRepository extends JpaRepository<Scheduler,String> , JpaSpecificationExecutor<Scheduler> {

    Scheduler findSchedulerByUuid(String uuid);

}
