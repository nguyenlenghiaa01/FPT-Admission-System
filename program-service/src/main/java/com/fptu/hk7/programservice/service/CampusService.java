package com.fptu.hk7.programservice.service;

import com.fptu.hk7.programservice.dto.Request.CampusRequest;
import com.fptu.hk7.programservice.exception.NotFoundException;
import com.fptu.hk7.programservice.pojo.Campus;
import com.fptu.hk7.programservice.repository.CampusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CampusService {
    @Autowired
    private CampusRepository campusRepository;

    public Campus createCampus(CampusRequest campusRequest) {
        Campus campus = new Campus();
        campus.setId(UUID.randomUUID());
        campus.setName(campusRequest.getCampusName());
        campus.setAddress(campusRequest.getAddress());
        campus.setDescription(campusRequest.getDescription());
        campus.setEmail(campusRequest.getEmail());
        campus.setPhone(campusRequest.getPhone());
        campus.setImageUrl(campusRequest.getImageUrl());
        return campusRepository.save(campus);
    }

    public List<Campus> getAllCampuses() {
        return campusRepository.findAll();
    }

    public Optional<Campus> getCampusById(UUID id) {
        return campusRepository.findById(id);
    }

    public Campus updateCampus(UUID id,CampusRequest campus) {
        Campus campus2 = campusRepository.findCampusById(id);
        if(campus2 == null){
            throw new NotFoundException("Campus not found");
        }
        campus2.setName(campus.getCampusName());
        campus2.setDescription(campus.getDescription());
        campus2.setAddress(campus.getAddress());
        campus2.setPhone(campus.getPhone());
        campus2.setEmail(campus.getEmail());
        campus2.setImageUrl(campus.getImageUrl());
        return campusRepository.save(campus2);
    }

    @Transactional
    public void deleteCampus(UUID id) {
        campusRepository.deleteById(id);
    }
}
