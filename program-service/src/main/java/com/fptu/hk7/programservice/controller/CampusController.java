package com.fptu.hk7.programservice.controller;

import com.fptu.hk7.programservice.InterFace.ICampusService;
import com.fptu.hk7.programservice.dto.Request.CampusRequest;
import com.fptu.hk7.programservice.pojo.Campus;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/campus")
@RequiredArgsConstructor
@SecurityRequirement(name = "api")
public class CampusController {
    private final ICampusService campusService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<Campus> createCampus(@RequestBody CampusRequest campus) {
        return ResponseEntity.ok(campusService.createCampus(campus));
    }

    @GetMapping("/get")
    public ResponseEntity<List<Campus>> getAllCampuses() {
        return ResponseEntity.ok(campusService.getAllCampuses());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Campus> getCampusById(@PathVariable("id") UUID id) {
        return campusService.getCampusById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<Campus> updateCampus(@PathVariable("id") UUID id, @RequestBody CampusRequest campus) {
        return ResponseEntity.ok(campusService.updateCampus(id,campus));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCampus(@PathVariable("id") UUID id) {
        campusService.deleteCampus(id);
        return ResponseEntity.noContent().build();
    }
}
