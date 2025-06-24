package com.fptu.hk7.programservice.controller;

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
    public ResponseEntity<Major> createMajor(@RequestBody Major major) {
        return ResponseEntity.ok(majorService.createMajor(major));
    }

    @GetMapping
    public ResponseEntity<List<Major>> getAllMajors() {
        return ResponseEntity.ok(majorService.getAllMajors());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Major> getMajorById(@PathVariable("id") UUID id) {
        return majorService.getMajorById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<Major> updateMajor(@PathVariable("id") UUID id, @RequestBody Major major) {
        major.setId(id);
        return ResponseEntity.ok(majorService.updateMajor(major));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMajor(@PathVariable("id") UUID id) {
        majorService.deleteMajor(id);
        return ResponseEntity.noContent().build();
    }
}
