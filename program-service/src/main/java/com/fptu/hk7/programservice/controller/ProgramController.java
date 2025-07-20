package com.fptu.hk7.programservice.controller;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fptu.hk7.programservice.InterFace.IOfferingService;
import com.fptu.hk7.programservice.dto.Request.OfferingRequest;
import com.fptu.hk7.programservice.dto.Response.GetOfferingResponse;
import com.fptu.hk7.programservice.dto.Response.OfferingDetail;
import com.fptu.hk7.programservice.exception.NotFoundException;
import com.fptu.hk7.programservice.pojo.Offering;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/program")
@RequiredArgsConstructor
@SecurityRequirement(name = "api")
@Transactional
public class ProgramController {

    private final IOfferingService offeringService;

    @PostMapping("/get_offering")
    @JsonFormat(shape = JsonFormat.Shape.OBJECT)
    public ResponseEntity<GetOfferingResponse> getOffering(@Validated @RequestBody OfferingRequest offeringRequest) {
        return offeringService.findOfferingByCampusNameAndSpecializationName(offeringRequest.getCampusUuid(), offeringRequest.getSpecializationUuid());
    }

    @GetMapping("/get_all")
    public ResponseEntity<List<Offering>> getAllOffering() {
        return ResponseEntity.ok(offeringService.getAllOfferings());
    }

    @GetMapping("/get_detail_offering/{id}")
    public ResponseEntity<OfferingDetail> getOfferingDetail(@PathVariable("id") UUID id) {
        Offering offering = offeringService.getOfferingById(id).orElseThrow(()-> new NotFoundException("Offering not found"));
        return ResponseEntity.ok(OfferingDetail.builder()
                        .campus(offering.getCampus().getName())
                        .major(offering.getSpecialization().getMajor().getName())
                        .specialization(offering.getSpecialization().getName())
                        .build());
    }

}