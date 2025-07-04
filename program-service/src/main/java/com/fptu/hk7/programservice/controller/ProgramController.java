package com.fptu.hk7.programservice.controller;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fptu.hk7.programservice.dto.Request.OfferingRequest;
import com.fptu.hk7.programservice.dto.Response.GetOfferingResponse;
import com.fptu.hk7.programservice.pojo.Offering;
import com.fptu.hk7.programservice.service.OfferingService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

    private final OfferingService offeringService;

    @PostMapping("/get_offering")
    @JsonFormat(shape = JsonFormat.Shape.OBJECT)
    public ResponseEntity<GetOfferingResponse> getOffering(@Validated @RequestBody OfferingRequest offeringRequest) {
        return offeringService.findOfferingByCampusNameAndSpecializationName(offeringRequest.getCampusUuid(), offeringRequest.getSpecializationUuid());
    }

    @GetMapping("/get_all")
    public ResponseEntity<List<Offering>> getAllOffering() {
        return ResponseEntity.ok(offeringService.getAllOfferings());
    }
}