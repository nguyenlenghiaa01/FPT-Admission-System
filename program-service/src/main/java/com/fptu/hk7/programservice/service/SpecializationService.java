package com.fptu.hk7.programservice.service;

import com.fptu.hk7.programservice.pojo.Specialization;
import com.fptu.hk7.programservice.repository.SpecializationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SpecializationService {
    @Autowired
    private SpecializationRepository specializationRepository;

    public Specialization createSpecialization(Specialization specialization) {
        return specializationRepository.save(specialization);
    }

    public List<Specialization> getAllSpecializations() {
        return specializationRepository.findAll();
    }

    public Optional<Specialization> getSpecializationById(Long id) {
        return specializationRepository.findById(id);
    }

    public Specialization updateSpecialization(Specialization specialization) {
        return specializationRepository.save(specialization);
    }

    public void deleteSpecialization(Long id) {
        specializationRepository.deleteById(id);
    }
}
