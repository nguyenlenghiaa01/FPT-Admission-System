package com.fptu.hk7.programservice.service;

import com.fptu.hk7.programservice.InterFace.ISpecializationService;
import com.fptu.hk7.programservice.dto.Request.SpecializationRequest;
import com.fptu.hk7.programservice.dto.Response.DataResponse;
import com.fptu.hk7.programservice.dto.Response.MajorResponse;
import com.fptu.hk7.programservice.dto.Response.SpecializationResponse;
import com.fptu.hk7.programservice.exception.NotFoundException;
import com.fptu.hk7.programservice.pojo.Campus;
import com.fptu.hk7.programservice.pojo.Major;
import com.fptu.hk7.programservice.pojo.Offering;
import com.fptu.hk7.programservice.pojo.Specialization;
import com.fptu.hk7.programservice.repository.CampusRepository;
import com.fptu.hk7.programservice.repository.MajorRepository;
import com.fptu.hk7.programservice.repository.OfferingRepository;
import com.fptu.hk7.programservice.repository.SpecializationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SpecializationService implements ISpecializationService {
    private final SpecializationRepository specializationRepository;
    private final MajorRepository majorRepository;

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

    public Specialization updateSpecialization(UUID id,SpecializationRequest specialization) {
        Specialization specialization1 = specializationRepository.findSpecializationById(id);
        if(specialization1 == null){
            throw new NotFoundException("Specialization not found");
        }
        specialization1.setName(specialization.getName());
        specialization1.setDescription(specialization.getDescription());
        specialization1.setMajor(specialization.getMajor());
        return specializationRepository.save(specialization1);
    }

    @Transactional
    public List<SpecializationResponse> getSpecializationByMajorId(UUID id){
        Major major = majorRepository.findMajorById(id);
        if(major == null){
            throw new NotFoundException("Major not found!");
        }

        List<SpecializationResponse> specializationResponses = new ArrayList<>();
        for(Specialization specialization : major.getSpecializations()){
            SpecializationResponse response = new SpecializationResponse();
            response.setSpecializationId(specialization.getId());
            response.setName(specialization.getName());
            response.setDescription(specialization.getDescription());
            response.setMajor(specialization.getMajor());

            specializationResponses.add(response);
        }

        return specializationResponses;
    }



    @Transactional
    public void deleteSpecialization(UUID id) {
        specializationRepository.deleteById(id);
    }
}
