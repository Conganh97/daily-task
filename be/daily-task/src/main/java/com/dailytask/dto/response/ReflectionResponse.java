package com.dailytask.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record ReflectionResponse(
    Long id,
    LocalDate date,
    Integer energyRating,
    String reflectionText,
    String username,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
} 