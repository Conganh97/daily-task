package com.dailytask.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public record UpdateEnergyAssessmentRequest(
    @Min(value = 1, message = "Energy level must be at least 1")
    @Max(value = 5, message = "Energy level must not exceed 5")
    Integer energyLevel
) {
} 