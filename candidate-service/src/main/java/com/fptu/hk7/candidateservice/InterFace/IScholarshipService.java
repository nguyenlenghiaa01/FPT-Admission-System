package com.fptu.hk7.candidateservice.InterFace;

import com.fptu.hk7.candidateservice.dto.request.ScholarshipRequest;
import com.fptu.hk7.candidateservice.pojo.Scholarship;

import java.util.List;
import java.util.UUID;

public interface IScholarshipService {
    Scholarship save(Scholarship scholarship);
    Scholarship getScholarshipById(UUID id);
    Scholarship getScholarshipByName(String name);
    List<Scholarship> getAllScholarships();
    Scholarship updateScholarship(UUID id, ScholarshipRequest updatedScholarship);
    String deleteScholarship(UUID id);
    long count();
}
