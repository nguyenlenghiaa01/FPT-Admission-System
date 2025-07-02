package com.fptu.hk7.programservice.repository;

import com.fptu.hk7.programservice.pojo.Campus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CampusRepository extends JpaRepository<Campus, Long> {
    Optional<Campus> findById(UUID id);
    Campus findCampusById(UUID id);
    void deleteById(UUID id);
}
