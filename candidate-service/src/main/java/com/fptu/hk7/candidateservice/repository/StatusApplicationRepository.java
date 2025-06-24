package com.fptu.hk7.candidateservice.repository;

import com.fptu.hk7.candidateservice.pojo.StatusApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface StatusApplicationRepository extends JpaRepository<StatusApplication, UUID> {
}
