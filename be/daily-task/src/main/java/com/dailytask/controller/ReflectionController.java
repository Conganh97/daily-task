package com.dailytask.controller;

import com.dailytask.dto.request.CreateReflectionRequest;
import com.dailytask.dto.response.ReflectionResponse;
import com.dailytask.service.ReflectionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/users/{username}/reflections")
@CrossOrigin(origins = "*")
public class ReflectionController {

    private final ReflectionService reflectionService;

    @Autowired
    public ReflectionController(ReflectionService reflectionService) {
        this.reflectionService = reflectionService;
    }

    @GetMapping
    public ResponseEntity<List<ReflectionResponse>> getReflections(
            @PathVariable String username,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) Integer minRating,
            @RequestParam(required = false) Integer maxRating) {
        
        List<ReflectionResponse> reflections;
        
        if (date != null) {
            ReflectionResponse reflection = reflectionService.getReflectionByUserAndDate(username, date);
            reflections = reflection != null ? List.of(reflection) : List.of();
        } else if (startDate != null && endDate != null) {
            reflections = reflectionService.getReflectionsByUserAndDateRange(username, startDate, endDate);
        } else if (minRating != null && maxRating != null) {
            reflections = reflectionService.getReflectionsByEnergyRating(username, minRating, maxRating);
        } else {
            reflections = reflectionService.getAllReflectionsByUser(username);
        }
        
        return ResponseEntity.ok(reflections);
    }

    @PostMapping
    public ResponseEntity<ReflectionResponse> createOrUpdateReflection(
            @PathVariable String username,
            @Valid @RequestBody CreateReflectionRequest request) {
        ReflectionResponse reflection = reflectionService.createOrUpdateReflection(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(reflection);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReflectionResponse> updateReflection(
            @PathVariable String username,
            @PathVariable Long id,
            @Valid @RequestBody CreateReflectionRequest request) {
        ReflectionResponse reflection = reflectionService.createOrUpdateReflection(request);
        return ResponseEntity.ok(reflection);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReflection(
            @PathVariable String username,
            @PathVariable Long id) {
        reflectionService.deleteReflection(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReflectionResponse> getReflectionById(
            @PathVariable String username,
            @PathVariable Long id) {
        ReflectionResponse reflection = reflectionService.getReflectionById(id);
        return ResponseEntity.ok(reflection);
    }

    @GetMapping("/today")
    public ResponseEntity<ReflectionResponse> getTodayReflection(@PathVariable String username) {
        ReflectionResponse reflection = reflectionService.getReflectionByUserAndDate(username, LocalDate.now());
        return ResponseEntity.ok(reflection);
    }

    @GetMapping("/exists")
    public ResponseEntity<Boolean> checkReflectionExists(
            @PathVariable String username,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        boolean exists = reflectionService.existsReflectionByUserAndDate(username, date);
        return ResponseEntity.ok(exists);
    }

    @GetMapping("/average-rating")
    public ResponseEntity<AverageRatingResponse> getAverageRating(
            @PathVariable String username,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        
        Double averageRating;
        if (startDate != null && endDate != null) {
            averageRating = reflectionService.getAverageEnergyRatingByUserAndDateRange(username, startDate, endDate);
        } else {
            averageRating = reflectionService.getAverageEnergyRatingByUser(username);
        }
        
        AverageRatingResponse response = new AverageRatingResponse(
            averageRating != null ? averageRating : 0.0,
            startDate,
            endDate
        );
        
        return ResponseEntity.ok(response);
    }

    public record AverageRatingResponse(
        Double averageRating,
        LocalDate startDate,
        LocalDate endDate
    ) {}
} 