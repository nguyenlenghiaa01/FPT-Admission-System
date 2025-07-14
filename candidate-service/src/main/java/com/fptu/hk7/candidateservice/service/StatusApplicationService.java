package com.fptu.hk7.candidateservice.service;

import com.fptu.hk7.candidateservice.InterFace.IScholarshipService;
import com.fptu.hk7.candidateservice.InterFace.IStatusApplicationService;
import com.fptu.hk7.candidateservice.pojo.StatusApplication;
import com.fptu.hk7.candidateservice.repository.StatusApplicationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StatusApplicationService implements IStatusApplicationService {
    private final StatusApplicationRepository statusApplicationRepository;
    public StatusApplication create(StatusApplication statusApplication) {
        return statusApplicationRepository.save(statusApplication);
    }
}
