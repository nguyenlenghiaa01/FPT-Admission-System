package com.fptu.hk7.candidateservice.InterFace;

import com.fptu.hk7.candidateservice.dto.request.ApplicationRequest;
import com.fptu.hk7.candidateservice.dto.response.ApplicationResponse;
import com.fptu.hk7.candidateservice.dto.response.ResponseApi;
import com.fptu.hk7.candidateservice.event.ReturnApplication;
import com.fptu.hk7.candidateservice.pojo.Application;
import com.fptu.hk7.candidateservice.pojo.Candidate;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface IApplicationService {
    Application createApplication(Application application);
    Application getApplicationById(UUID id);
    List<Application> getAllApplications();
    List<Application> getApplicationsByCandidate(Candidate candidate);
    Application updateApplication(UUID id, Application updatedApplication);
    boolean deleteApplication(UUID id);
    ResponseEntity<ResponseApi<ApplicationResponse>> submitApplication(ApplicationRequest applicationRequest);
    void returnStatusApplication(ReturnApplication returnApplication);
}
