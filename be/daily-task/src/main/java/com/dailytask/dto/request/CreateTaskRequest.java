package com.dailytask.dto.request;

import com.dailytask.validation.ValidDate;
import com.dailytask.validation.ValidUsername;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record CreateTaskRequest(
    @NotBlank(message = "Title is required")
    @Size(max = 200, message = "Title must not exceed 200 characters")
    String title,
    
    @Size(max = 1000, message = "Description must not exceed 1000 characters")
    String description,
    
    @NotNull(message = "Date is required")
    @ValidDate(maxDaysInFuture = 365, message = "Task date cannot be more than 1 year in the future")
    LocalDate date,
    
    @NotBlank(message = "Username is required")
    @ValidUsername
    String username
) {
} 