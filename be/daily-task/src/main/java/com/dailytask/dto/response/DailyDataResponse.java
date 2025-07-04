package com.dailytask.dto.response;

import java.time.LocalDate;
import java.util.List;

public record DailyDataResponse(
    String username,
    LocalDate date,
    List<TaskResponse> tasks,
    ReflectionResponse reflection,
    EnergyAssessmentResponse energyAssessment,
    DailyStats stats
) {
    public record DailyStats(
        long totalTasks,
        long completedTasks,
        long incompleteTasks,
        long starredTasks,
        Double averageEnergyRating,
        Double averageEnergyLevel
    ) {}
} 