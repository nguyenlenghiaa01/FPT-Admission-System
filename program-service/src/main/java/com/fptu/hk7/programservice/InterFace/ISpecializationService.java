package com.fptu.hk7.programservice.InterFace;

import com.fptu.hk7.programservice.dto.Request.SpecializationRequest;
import com.fptu.hk7.programservice.dto.Response.DataResponse;
import com.fptu.hk7.programservice.dto.Response.SpecializationResponse;
import com.fptu.hk7.programservice.pojo.Specialization;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ISpecializationService {
    Specialization createSpecialization(SpecializationRequest specializationRequest);
    DataResponse<SpecializationResponse> getAllSpecializations(int page, int size);
    Optional<Specialization> getSpecializationById(UUID id);
    Specialization updateSpecialization(UUID id,SpecializationRequest specialization);
    List<SpecializationResponse> getSpecializationByMajorId(UUID id);
    void deleteSpecialization(UUID id);
}
