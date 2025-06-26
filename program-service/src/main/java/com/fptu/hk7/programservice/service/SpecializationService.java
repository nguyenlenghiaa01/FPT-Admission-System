package com.fptu.hk7.programservice.service;

import com.fptu.hk7.programservice.dto.Request.SpecializationRequest;
import com.fptu.hk7.programservice.dto.Response.DataResponse;
import com.fptu.hk7.programservice.dto.Response.MajorResponse;
import com.fptu.hk7.programservice.dto.Response.SpecializationResponse;
import com.fptu.hk7.programservice.pojo.Major;
import com.fptu.hk7.programservice.pojo.Specialization;
import com.fptu.hk7.programservice.repository.SpecializationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class SpecializationService {
    @Autowired
    private SpecializationRepository specializationRepository;

    public Specialization createSpecialization(SpecializationRequest specializationRequest) {
        Specialization specialization =new Specialization();
        specialization.setId(UUID.randomUUID());
        specialization.setName(specializationRequest.getName());
        specialization.setDescription(specializationRequest.getDescription());
        specialization.setMajor(specializationRequest.getMajor());
        return specializationRepository.save(specialization);
    }

    public DataResponse<SpecializationResponse> getAllSpecializations(int page, int size) {
        Page<Specialization> specializationPage = specializationRepository.findAll(PageRequest.of(page, size));
        List<Specialization> specializations = specializationPage.getContent();
        List<SpecializationResponse> specializationResponses = new ArrayList<>();
        for(Specialization specialization: specializations) {
            SpecializationResponse specializationResponse = new SpecializationResponse();
            specializationResponse.setSpecializationId(specialization.getId());
            specializationResponse.setName(specialization.getName());
            specializationResponse.setDescription(specialization.getDescription());
            specializationResponse.setMajor(specialization.getMajor());

            specializationResponses.add(specializationResponse);
        }

        DataResponse<SpecializationResponse> dataResponse = new DataResponse<>();
        dataResponse.setListData(specializationResponses);
        dataResponse.setTotalElements(specializationPage.getTotalElements());
        dataResponse.setPageNumber(specializationPage.getNumber());
        dataResponse.setTotalPages(specializationPage.getTotalPages());
        return dataResponse;
    }

    public Optional<Specialization> getSpecializationById(UUID id) {
        return specializationRepository.findById(id);
    }

    public Specialization updateSpecialization(Specialization specialization) {
        return specializationRepository.save(specialization);
    }

    public void deleteSpecialization(UUID id) {
        specializationRepository.deleteById(id);
    }
}
