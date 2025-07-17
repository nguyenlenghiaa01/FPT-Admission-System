package com.fptu.hk7.candidateservice.repository;


import com.fptu.hk7.candidateservice.pojo.Application;
import com.fptu.hk7.candidateservice.pojo.Candidate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, UUID> {
    List<Application> findAllByCandidate(Candidate candidate);

    Optional<Application> findApplicationByBookingUuid(UUID uuid);
}
