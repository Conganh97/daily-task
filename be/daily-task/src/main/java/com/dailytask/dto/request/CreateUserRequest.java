package com.dailytask.dto.request;

import com.dailytask.validation.ValidUsername;
import jakarta.validation.constraints.NotBlank;

public record CreateUserRequest(
    @NotBlank(message = "Username is required")
    @ValidUsername
    String username
) {
} 