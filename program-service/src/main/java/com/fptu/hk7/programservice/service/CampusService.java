package com.fptu.hk7.programservice.service;

import com.fptu.hk7.programservice.pojo.Campus;
import com.fptu.hk7.programservice.repository.CampusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CampusService {
    @Autowired
    private CampusRepository campusRepository;

    public Campus createCampus(Campus campus) {
        return campusRepository.save(campus);
    }

    public List<Campus> getAllCampuses() {
        return campusRepository.findAll();
    }

    public Optional<Campus> getCampusById(Long id) {
        return campusRepository.findById(id);
    }

    public Campus updateCampus(Campus campus) {
        return campusRepository.save(campus);
    }

    public void deleteCampus(Long id) {
        campusRepository.deleteById(id);
    }
}
