package com.dailytask.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record TaskResponse(
    Long id,
    String title,
    String description,
    LocalDate date,
    boolean completed,
    boolean starred,
    String username,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
} 