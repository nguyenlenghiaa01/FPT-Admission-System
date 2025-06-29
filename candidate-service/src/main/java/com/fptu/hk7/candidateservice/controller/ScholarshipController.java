package com.fptu.hk7.candidateservice.controller;

import com.fptu.hk7.candidateservice.dto.request.ScholarshipRequest;
import com.fptu.hk7.candidateservice.pojo.Scholarship;
import com.fptu.hk7.candidateservice.service.ScholarshipService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/scholarship")
@RequiredArgsConstructor
@SecurityRequirement(name = "api")
public class ScholarshipController {
    private final ScholarshipService scholarshipService;

    @GetMapping
    public List<Scholarship> getAllScholarShip(){
        return scholarshipService.getAllScholarships();
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteScholarship(@PathVariable("id") UUID id){
       return ResponseEntity.ok(scholarshipService.deleteScholarship(id));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateScholarship(@Validated @RequestBody ScholarshipRequest scholarshipRequest, @PathVariable("id") UUID id){
        return ResponseEntity.ok(scholarshipService.updateScholarship(id, scholarshipRequest));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    public ResponseEntity<?> createScholarship(@Validated @RequestBody ScholarshipRequest scholarshipRequest){
        return ResponseEntity.ok(scholarshipService.save(new Scholarship(
                scholarshipRequest.getName(),
                scholarshipRequest.getDescription()
        )));
    }
}
