package com.dailytask.dto.request;

import com.dailytask.validation.ValidDate;
import com.dailytask.validation.ValidUsername;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record CreateReflectionRequest(
    @NotNull(message = "Date is required")
    @ValidDate(maxDaysInPast = 30, message = "Cannot create reflections older than 30 days")
    LocalDate date,
    
    @NotNull(message = "Energy rating is required")
    @Min(value = 1, message = "Energy rating must be at least 1")
    @Max(value = 10, message = "Energy rating must not exceed 10")
    Integer energyRating,
    
    @Size(max = 2000, message = "Reflection text must not exceed 2000 characters")
    String reflectionText,
    
    @NotBlank(message = "Username is required")
    @ValidUsername
    String username
) {
} 