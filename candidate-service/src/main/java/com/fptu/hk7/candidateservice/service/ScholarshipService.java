package com.fptu.hk7.candidateservice.service;

import com.fptu.hk7.candidateservice.dto.request.ScholarshipRequest;
import com.fptu.hk7.candidateservice.pojo.Scholarship;
import com.fptu.hk7.candidateservice.repository.ScholarshipRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Lazy
@RequiredArgsConstructor
public class ScholarshipService {
    private final ScholarshipRepository scholarshipRepository;

    public Scholarship save(Scholarship scholarship) {
        return scholarshipRepository.save(scholarship);
    }

    public Scholarship getScholarshipById(UUID id) {
        return scholarshipRepository.findById(id).orElse(null);
    }

    public Scholarship getScholarshipByName(String name) {
        return scholarshipRepository.findByName(name).orElse(null);
    }

    public List<Scholarship> getAllScholarships() {
        return scholarshipRepository.findAll();
    }

    public Scholarship updateScholarship(UUID id, ScholarshipRequest updatedScholarship) {
        return scholarshipRepository.findById(id)
                .map(scholarship -> {
                    scholarship.setName(updatedScholarship.getName());
                    scholarship.setDescription(updatedScholarship.getDescription());
                    return scholarshipRepository.save(scholarship);
                })
                .orElse(null);
    }

    @Transactional
    public String deleteScholarship(UUID id) {
        if (scholarshipRepository.existsById(id)) {
            scholarshipRepository.deleteById(id);
        }
        return "Delete successfully";
    }

    public long count() {
        return scholarshipRepository.count();
    }
}
