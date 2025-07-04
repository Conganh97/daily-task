package com.dailytask.dto.request;

import jakarta.validation.constraints.Size;

public record UpdateTaskRequest(
    @Size(max = 200, message = "Title must not exceed 200 characters")
    String title,
    
    @Size(max = 1000, message = "Description must not exceed 1000 characters")
    String description,
    
    Boolean completed,
    
    Boolean starred
) {
} 