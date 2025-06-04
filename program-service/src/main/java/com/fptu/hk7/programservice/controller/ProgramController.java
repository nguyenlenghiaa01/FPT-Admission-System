package com.fptu.hk7.programservice.controller;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fptu.hk7.programservice.dto.Request.OfferingRequest;
import com.fptu.hk7.programservice.pojo.Offering;
import com.fptu.hk7.programservice.service.OfferingService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/program")
@RequiredArgsConstructor
@Transactional
public class ProgramController {

    private final OfferingService offeringService;

    @PostMapping("/get_offering")
    @JsonFormat(shape = JsonFormat.Shape.OBJECT)
    public ResponseEntity<UUID> getOffering(@Validated @RequestBody OfferingRequest offeringRequest) {
        return offeringService.findOfferingByCampusNameAndSpecializationName(offeringRequest.getCampus(), offeringRequest.getSpecialization());
    }
}