package com.fptu.hk7.programservice.service;

import com.fptu.hk7.programservice.dto.Request.CampusRequest;
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
        return campusRepository.save(campus);
    }

    public List<Campus> getAllCampuses() {
        return campusRepository.findAll();
    }

    public Optional<Campus> getCampusById(UUID id) {
        return campusRepository.findById(id);
    }

    public Campus updateCampus(Campus campus) {
        return campusRepository.save(campus);
    }

    @Transactional
    public void deleteCampus(UUID id) {
        campusRepository.deleteById(id);
    }
}
