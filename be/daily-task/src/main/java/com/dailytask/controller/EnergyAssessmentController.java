package com.dailytask.controller;

import com.dailytask.dto.request.CreateEnergyAssessmentRequest;
import com.dailytask.dto.response.EnergyAssessmentResponse;
import com.dailytask.service.EnergyAssessmentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/users/{username}/energy")
@CrossOrigin(origins = "*")
public class EnergyAssessmentController {

    private final EnergyAssessmentService energyAssessmentService;

    @Autowired
    public EnergyAssessmentController(EnergyAssessmentService energyAssessmentService) {
        this.energyAssessmentService = energyAssessmentService;
    }

    @GetMapping
    public ResponseEntity<List<EnergyAssessmentResponse>> getEnergyAssessments(
            @PathVariable String username,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) Integer minLevel,
            @RequestParam(required = false) Integer maxLevel) {
        
        List<EnergyAssessmentResponse> energyAssessments;
        
        if (date != null) {
            EnergyAssessmentResponse assessment = energyAssessmentService.getEnergyAssessmentByUserAndDate(username, date);
            energyAssessments = assessment != null ? List.of(assessment) : List.of();
        } else if (startDate != null && endDate != null) {
            energyAssessments = energyAssessmentService.getEnergyAssessmentsByUserAndDateRange(username, startDate, endDate);
        } else if (minLevel != null && maxLevel != null) {
            energyAssessments = energyAssessmentService.getEnergyAssessmentsByEnergyLevel(username, minLevel, maxLevel);
        } else {
            energyAssessments = energyAssessmentService.getAllEnergyAssessmentsByUser(username);
        }
        
        return ResponseEntity.ok(energyAssessments);
    }

    @PostMapping
    public ResponseEntity<EnergyAssessmentResponse> createOrUpdateEnergyAssessment(
            @PathVariable String username,
            @Valid @RequestBody CreateEnergyAssessmentRequest request) {
        EnergyAssessmentResponse assessment = energyAssessmentService.createOrUpdateEnergyAssessment(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(assessment);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EnergyAssessmentResponse> updateEnergyAssessment(
            @PathVariable String username,
            @PathVariable Long id,
            @Valid @RequestBody CreateEnergyAssessmentRequest request) {
        EnergyAssessmentResponse assessment = energyAssessmentService.createOrUpdateEnergyAssessment(request);
        return ResponseEntity.ok(assessment);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEnergyAssessment(
            @PathVariable String username,
            @PathVariable Long id) {
        energyAssessmentService.deleteEnergyAssessment(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<EnergyAssessmentResponse> getEnergyAssessmentById(
            @PathVariable String username,
            @PathVariable Long id) {
        EnergyAssessmentResponse assessment = energyAssessmentService.getEnergyAssessmentById(id);
        return ResponseEntity.ok(assessment);
    }

    @GetMapping("/today")
    public ResponseEntity<EnergyAssessmentResponse> getTodayEnergyAssessment(@PathVariable String username) {
        EnergyAssessmentResponse assessment = energyAssessmentService.getEnergyAssessmentByUserAndDate(username, LocalDate.now());
        return ResponseEntity.ok(assessment);
    }

    @GetMapping("/exists")
    public ResponseEntity<Boolean> checkEnergyAssessmentExists(
            @PathVariable String username,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        boolean exists = energyAssessmentService.existsEnergyAssessmentByUserAndDate(username, date);
        return ResponseEntity.ok(exists);
    }

    @GetMapping("/average-level")
    public ResponseEntity<AverageEnergyLevelResponse> getAverageEnergyLevel(
            @PathVariable String username,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        
        Double averageLevel;
        if (startDate != null && endDate != null) {
            averageLevel = energyAssessmentService.getAverageEnergyLevelByUserAndDateRange(username, startDate, endDate);
        } else {
            averageLevel = energyAssessmentService.getAverageEnergyLevelByUser(username);
        }
        
        AverageEnergyLevelResponse response = new AverageEnergyLevelResponse(
            averageLevel != null ? averageLevel : 0.0,
            startDate,
            endDate
        );
        
        return ResponseEntity.ok(response);
    }

    public record AverageEnergyLevelResponse(
        Double averageLevel,
        LocalDate startDate,
        LocalDate endDate
    ) {}
} 