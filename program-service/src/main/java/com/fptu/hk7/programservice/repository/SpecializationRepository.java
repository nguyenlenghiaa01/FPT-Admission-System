package com.fptu.hk7.programservice.repository;

import com.fptu.hk7.programservice.pojo.Specialization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SpecializationRepository extends JpaRepository<Specialization, Long> {
    Optional<Specialization> findSpecializationIdByName(String specializationName);
}
