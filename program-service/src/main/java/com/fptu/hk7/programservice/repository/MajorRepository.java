package com.fptu.hk7.programservice.repository;

import com.fptu.hk7.programservice.pojo.Major;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface MajorRepository extends JpaRepository<Major, Long> {
    Optional<Major> findById(UUID id);

    void deleteById(UUID id);
}
