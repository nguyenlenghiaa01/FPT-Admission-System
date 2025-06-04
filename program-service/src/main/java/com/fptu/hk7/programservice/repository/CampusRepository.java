package com.fptu.hk7.programservice.repository;

import com.fptu.hk7.programservice.pojo.Campus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CampusRepository extends JpaRepository<Campus, Long> {

    Optional<Campus> findCampusIdByName(String campusName);
}
