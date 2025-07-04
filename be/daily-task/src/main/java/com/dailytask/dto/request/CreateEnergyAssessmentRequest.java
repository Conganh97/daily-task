package com.dailytask.dto.request;

import com.dailytask.validation.ValidDate;
import com.dailytask.validation.ValidUsername;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record CreateEnergyAssessmentRequest(
    @NotNull(message = "Date is required")
    @ValidDate(maxDaysInPast = 30, message = "Cannot create energy assessments older than 30 days")
    LocalDate date,
    
    @NotNull(message = "Energy level is required")
    @Min(value = 1, message = "Energy level must be at least 1")
    @Max(value = 5, message = "Energy level must not exceed 5")
    Integer energyLevel,
    
    @NotBlank(message = "Username is required")
    @ValidUsername
    String username
) {
} 