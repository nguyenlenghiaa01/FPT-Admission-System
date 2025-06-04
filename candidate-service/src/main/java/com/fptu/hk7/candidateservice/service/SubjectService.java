package com.fptu.hk7.candidateservice.service;

import com.fptu.hk7.candidateservice.pojo.Subject;
import com.fptu.hk7.candidateservice.repository.SubjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SubjectService {
    private final SubjectRepository subjectRepository;

    // Create
    public Subject createSubject(Subject subject) {
        return subjectRepository.save(subject);
    }

    // Read (find by id)
    public Optional<Subject> getSubjectById(UUID id) {
        return subjectRepository.findById(id);
    }

    // Read (find all)
    public List<Subject> getAllSubjects() {
        return subjectRepository.findAll();
    }

    // Delete
    public boolean deleteSubject(UUID id) {
        if (subjectRepository.existsById(id)) {
            subjectRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
