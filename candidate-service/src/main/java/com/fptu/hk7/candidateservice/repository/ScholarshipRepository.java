package com.fptu.hk7.candidateservice.repository;

import com.fptu.hk7.candidateservice.pojo.Scholarship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ScholarshipRepository extends JpaRepository<Scholarship, UUID> {
    Optional<Scholarship> findByName(String name);
}
