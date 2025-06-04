package com.fptu.hk7.programservice.repository;

import com.fptu.hk7.programservice.pojo.Offering;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface OfferingRepository extends JpaRepository<Offering, UUID> {
    Optional<Offering> findOfferingByCampusIdAndSpecializationId(UUID campusId, UUID specializationId);
}
