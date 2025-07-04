package com.dailytask.dto.response;

import java.time.LocalDateTime;

public record UserResponse(
    Long id,
    String username,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
} 