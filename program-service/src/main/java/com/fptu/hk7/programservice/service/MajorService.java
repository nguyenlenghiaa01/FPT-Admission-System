package com.fptu.hk7.programservice.service;

import com.fptu.hk7.programservice.pojo.Major;
import com.fptu.hk7.programservice.repository.MajorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class MajorService {
    @Autowired
    private MajorRepository majorRepository;

    public Major createMajor(Major major) {
        return majorRepository.save(major);
    }

    public List<Major> getAllMajors() {
        return majorRepository.findAll();
    }

    public Optional<Major> getMajorById(UUID id) {
        return majorRepository.findById(id);
    }

    public Major updateMajor(Major major) {
        return majorRepository.save(major);
    }

    public void deleteMajor(UUID id) {
        majorRepository.deleteById(id);
    }
}
