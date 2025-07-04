package com.dailytask.service;

import com.dailytask.dto.request.CreateReflectionRequest;
import com.dailytask.dto.response.ReflectionResponse;

import java.time.LocalDate;
import java.util.List;

public interface ReflectionService {
    ReflectionResponse createOrUpdateReflection(CreateReflectionRequest request);
    ReflectionResponse getReflectionById(Long id);
    ReflectionResponse getReflectionByUserAndDate(String username, LocalDate date);
    List<ReflectionResponse> getReflectionsByUserAndDateRange(String username, LocalDate startDate, LocalDate endDate);
    List<ReflectionResponse> getAllReflectionsByUser(String username);
    List<ReflectionResponse> getReflectionsByEnergyRating(String username, Integer minRating, Integer maxRating);
    void deleteReflection(Long id);
    boolean existsReflectionByUserAndDate(String username, LocalDate date);
    Double getAverageEnergyRatingByUser(String username);
    Double getAverageEnergyRatingByUserAndDateRange(String username, LocalDate startDate, LocalDate endDate);
} 