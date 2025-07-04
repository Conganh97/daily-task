package com.dailytask.service;

import com.dailytask.dto.request.CreateEnergyAssessmentRequest;
import com.dailytask.dto.response.EnergyAssessmentResponse;

import java.time.LocalDate;
import java.util.List;

public interface EnergyAssessmentService {
    EnergyAssessmentResponse createOrUpdateEnergyAssessment(CreateEnergyAssessmentRequest request);
    EnergyAssessmentResponse getEnergyAssessmentById(Long id);
    EnergyAssessmentResponse getEnergyAssessmentByUserAndDate(String username, LocalDate date);
    List<EnergyAssessmentResponse> getEnergyAssessmentsByUserAndDateRange(String username, LocalDate startDate, LocalDate endDate);
    List<EnergyAssessmentResponse> getAllEnergyAssessmentsByUser(String username);
    List<EnergyAssessmentResponse> getEnergyAssessmentsByEnergyLevel(String username, Integer minLevel, Integer maxLevel);
    void deleteEnergyAssessment(Long id);
    boolean existsEnergyAssessmentByUserAndDate(String username, LocalDate date);
    Double getAverageEnergyLevelByUser(String username);
    Double getAverageEnergyLevelByUserAndDateRange(String username, LocalDate startDate, LocalDate endDate);
} 