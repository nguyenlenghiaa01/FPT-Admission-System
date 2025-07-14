package com.fptu.hk7.candidateservice.service;

import com.fptu.hk7.candidateservice.InterFace.ICandidateService;
import com.fptu.hk7.candidateservice.exception.NotFoundException;
import com.fptu.hk7.candidateservice.pojo.Candidate;
import com.fptu.hk7.candidateservice.repository.CandidateRepository;
import com.fptu.hk7.candidateservice.repository.SubjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CandidateService implements ICandidateService {
    public final CandidateRepository candidateRepository;

    // Create
    public Candidate createCandidate(Candidate candidate) {
        return candidateRepository.save(candidate);
    }

    // Read (find by id)
    public Candidate getCandidateById(UUID id) {
        return candidateRepository.findById(id).orElseThrow(() -> new NotFoundException("Candidate not found with id: " + id));
    }

    // Read (find all)
    public List<Candidate> getAllCandidates() {
        return candidateRepository.findAll();
    }

    // Update
    public Optional<Candidate> updateCandidate(UUID id, Candidate updatedCandidate) {
        return candidateRepository.findById(id).map(candidate -> {
            candidate.setEmail(updatedCandidate.getEmail());
            candidate.setFullname(updatedCandidate.getFullname());
            candidate.setCreateAt(updatedCandidate.getCreateAt());
            return candidateRepository.save(candidate);
        });
    }

    // Delete
    public boolean deleteCandidate(UUID id) {
        if (candidateRepository.existsById(id)) {
            candidateRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public String getCurrentEmailUser() {
        return SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
    }
}
