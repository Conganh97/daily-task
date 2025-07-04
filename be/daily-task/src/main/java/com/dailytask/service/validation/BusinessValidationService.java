package com.dailytask.service.validation;

import com.dailytask.entity.User;
import com.dailytask.exception.BusinessRuleViolationException;
import com.dailytask.repository.TaskRepository;
import com.dailytask.repository.ReflectionRepository;
import com.dailytask.repository.EnergyAssessmentRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class BusinessValidationService {

    private final TaskRepository taskRepository;
    private final ReflectionRepository reflectionRepository;
    private final EnergyAssessmentRepository energyAssessmentRepository;

    public BusinessValidationService(TaskRepository taskRepository,
                                   ReflectionRepository reflectionRepository,
                                   EnergyAssessmentRepository energyAssessmentRepository) {
        this.taskRepository = taskRepository;
        this.reflectionRepository = reflectionRepository;
        this.energyAssessmentRepository = energyAssessmentRepository;
    }

    public void validateTaskCreation(User user, LocalDate date) {
        // Check if user has too many tasks for a single day
        long taskCount = taskRepository.countByUserAndDate(user, date);
        if (taskCount >= 50) {
            throw new BusinessRuleViolationException(
                "Cannot create more than 50 tasks per day. Current count: " + taskCount);
        }
    }

    public void validateReflectionCreation(User user, LocalDate date) {
        // Check if reflection already exists for this date
        if (reflectionRepository.existsByUserAndDate(user, date)) {
            throw new BusinessRuleViolationException(
                "Reflection already exists for date: " + date + ". Use update operation instead.");
        }
        
        // Validate reflection date is not too far in the future
        if (date.isAfter(LocalDate.now().plusDays(1))) {
            throw new BusinessRuleViolationException(
                "Cannot create reflections more than 1 day in the future");
        }
    }

    public void validateEnergyAssessmentCreation(User user, LocalDate date) {
        // Check if energy assessment already exists for this date
        if (energyAssessmentRepository.existsByUserAndDate(user, date)) {
            throw new BusinessRuleViolationException(
                "Energy assessment already exists for date: " + date + ". Use update operation instead.");
        }
        
        // Validate assessment date is not too far in the future
        if (date.isAfter(LocalDate.now().plusDays(1))) {
            throw new BusinessRuleViolationException(
                "Cannot create energy assessments more than 1 day in the future");
        }
    }

    public void validateUserDeletion(User user) {
        // Check if user has recent activity
        long recentTasks = taskRepository.countByUserAndDateBetween(
            user, LocalDate.now().minusDays(7), LocalDate.now());
        
        if (recentTasks > 0) {
            throw new BusinessRuleViolationException(
                "Cannot delete user with recent activity. Found " + recentTasks + " tasks in the last 7 days.");
        }
    }

    public void validateDateRange(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("Start date and end date cannot be null");
        }
        
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Start date cannot be after end date");
        }
        
        // Limit date range queries to prevent performance issues
        if (startDate.isBefore(LocalDate.now().minusYears(2))) {
            throw new BusinessRuleViolationException(
                "Cannot query data older than 2 years");
        }
        
        long daysBetween = java.time.temporal.ChronoUnit.DAYS.between(startDate, endDate);
        if (daysBetween > 365) {
            throw new BusinessRuleViolationException(
                "Date range cannot exceed 365 days. Current range: " + daysBetween + " days");
        }
    }
} 