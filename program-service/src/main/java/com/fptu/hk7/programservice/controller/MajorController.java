package com.fptu.hk7.programservice.controller;

import com.fptu.hk7.programservice.dto.Request.MajorRequest;
import com.fptu.hk7.programservice.dto.Response.DataResponse;
import com.fptu.hk7.programservice.dto.Response.MajorResponse;
import com.fptu.hk7.programservice.pojo.Major;
import com.fptu.hk7.programservice.service.MajorService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/major")
@RequiredArgsConstructor
@SecurityRequirement(name = "api")
public class MajorController {

    private final MajorService majorService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<Major> createMajor(@RequestBody MajorRequest major) {
        return ResponseEntity.ok(majorService.createMajor(major));
    }

    @GetMapping
    public ResponseEntity <DataResponse<MajorResponse>> getAllMajors(
            @RequestParam int page,
            @RequestParam int size) {
        DataResponse<MajorResponse> majorResponse = majorService.getAllMajors(page,size);

        return ResponseEntity.ok(majorResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Major> getMajorById(@PathVariable("id") UUID id) {
        return majorService.getMajorById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<Major> updateMajor(@PathVariable("id") UUID id, @RequestBody MajorRequest major) {
        return ResponseEntity.ok(majorService.updateMajor(id,major));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMajor(@PathVariable("id") UUID id) {
        majorService.deleteMajor(id);
        return ResponseEntity.noContent().build();
    }
}
