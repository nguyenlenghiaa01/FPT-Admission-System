package com.fptu.hk7.programservice.controller;

import com.fptu.hk7.programservice.dto.Request.SpecializationRequest;
import com.fptu.hk7.programservice.dto.Response.DataResponse;
import com.fptu.hk7.programservice.dto.Response.SpecializationResponse;
import com.fptu.hk7.programservice.pojo.Specialization;
import com.fptu.hk7.programservice.service.SpecializationService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/specialization")
@RequiredArgsConstructor
@SecurityRequirement(name = "api")
public class SpecializationController {
    private final SpecializationService specializationService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<Specialization> createSpecialization(@RequestBody SpecializationRequest specialization) {
        return ResponseEntity.ok(specializationService.createSpecialization(specialization));
    }

    @GetMapping
    public ResponseEntity<DataResponse<SpecializationResponse>> getAllSpecializations(
            @RequestParam int page,
            @RequestParam int size) {
        DataResponse<SpecializationResponse> specializationDataResponse = specializationService.getAllSpecializations(page, size);
        return ResponseEntity.ok(specializationDataResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Specialization> getSpecializationById(@PathVariable UUID id) {
        return specializationService.getSpecializationById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<Specialization> updateSpecialization(@PathVariable UUID id, @RequestBody SpecializationRequest specialization) {
        return ResponseEntity.ok(specializationService.updateSpecialization(id,specialization));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSpecialization(@PathVariable UUID id) {
        specializationService.deleteSpecialization(id);
        return ResponseEntity.noContent().build();
    }
}
