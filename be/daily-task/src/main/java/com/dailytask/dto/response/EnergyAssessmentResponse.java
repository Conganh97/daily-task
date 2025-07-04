package com.dailytask.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record EnergyAssessmentResponse(
    Long id,
    LocalDate date,
    Integer energyLevel,
    String username,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
} 