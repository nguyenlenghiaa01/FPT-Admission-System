package com.fptu.hk7.programservice.InterFace;

import com.fptu.hk7.programservice.dto.Response.GetOfferingResponse;
import com.fptu.hk7.programservice.pojo.Offering;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IOfferingService {
    List<Offering> getAllOfferings();
    Optional<Offering> getOfferingById(UUID id);
    Offering updateOffering(Offering offering);
    void deleteOffering(UUID id);
    ResponseEntity<GetOfferingResponse> findOfferingByCampusNameAndSpecializationName(String campusUuid, String specializationUuid);
}
