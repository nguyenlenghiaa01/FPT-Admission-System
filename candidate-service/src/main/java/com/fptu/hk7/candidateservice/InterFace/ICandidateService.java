package com.fptu.hk7.candidateservice.InterFace;

import com.fptu.hk7.candidateservice.pojo.Candidate;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ICandidateService {
    Candidate createCandidate(Candidate candidate);
    Candidate getCandidateById(UUID id);
    List<Candidate> getAllCandidates();
    Optional<Candidate> updateCandidate(UUID id, Candidate updatedCandidate);
    boolean deleteCandidate(UUID id);
    String getCurrentUuid();
}
