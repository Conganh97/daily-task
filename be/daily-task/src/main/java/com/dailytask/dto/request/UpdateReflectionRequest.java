package com.dailytask.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

public record UpdateReflectionRequest(
    @Min(value = 1, message = "Energy rating must be at least 1")
    @Max(value = 10, message = "Energy rating must not exceed 10")
    Integer energyRating,
    
    @Size(max = 2000, message = "Reflection text must not exceed 2000 characters")
    String reflectionText
) {
} 