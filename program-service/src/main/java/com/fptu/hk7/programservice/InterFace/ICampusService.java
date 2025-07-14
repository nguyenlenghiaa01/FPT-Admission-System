package com.fptu.hk7.programservice.InterFace;

import com.fptu.hk7.programservice.dto.Request.CampusRequest;
import com.fptu.hk7.programservice.pojo.Campus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ICampusService {
    Campus createCampus(CampusRequest campusRequest);
    List<Campus> getAllCampuses();
    Optional<Campus> getCampusById(UUID id);
    Campus updateCampus(UUID id,CampusRequest campus);
    void deleteCampus(UUID id);
}
