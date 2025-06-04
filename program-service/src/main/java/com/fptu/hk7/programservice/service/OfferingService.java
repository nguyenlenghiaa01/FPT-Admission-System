package com.fptu.hk7.programservice.service;

import com.fptu.hk7.programservice.exception.NotFoundException;
import com.fptu.hk7.programservice.pojo.Campus;
import com.fptu.hk7.programservice.pojo.Offering;
import com.fptu.hk7.programservice.pojo.Specialization;
import com.fptu.hk7.programservice.repository.CampusRepository;
import com.fptu.hk7.programservice.repository.OfferingRepository;
import com.fptu.hk7.programservice.repository.SpecializationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OfferingService {
    private final OfferingRepository offeringRepo;
    private final CampusRepository campusRepo;
    private final SpecializationRepository specializationRepo;

    public Offering createOffering(Offering offering) {
        return offeringRepo.save(offering);
    }

    public List<Offering> getAllOfferings() {
        return offeringRepo.findAll();
    }

    public Optional<Offering> getOfferingById(UUID id) {
        return offeringRepo.findById(id);
    }

    public Offering updateOffering(Offering offering) {
        return offeringRepo.save(offering);
    }

    public void deleteOffering(UUID id) {
        offeringRepo.deleteById(id);
    }

    public ResponseEntity<UUID> findOfferingByCampusNameAndSpecializationName(String campusName, String specializationName) {
        Campus campus = campusRepo.findCampusIdByName(campusName)
                .orElseThrow(() -> new NotFoundException("Campus not found: " + campusName));
        Specialization specialization = specializationRepo.findSpecializationIdByName(specializationName)
                .orElseThrow(() -> new NotFoundException("Specialization not found: " + specializationName));
        Offering offering =  offeringRepo.findOfferingByCampusIdAndSpecializationId(campus.getId(), specialization.getId())
                .orElseThrow(() -> new NotFoundException("Offering not found for campus: " + campusName + " and specialization: " + specializationName));
        return ResponseEntity.ok(offering.getId());
    }
}
