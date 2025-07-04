package com.dailytask.controller;

import com.dailytask.dto.response.DailyDataResponse;
import com.dailytask.dto.response.EnergyAssessmentResponse;
import com.dailytask.dto.response.ReflectionResponse;
import com.dailytask.dto.response.TaskResponse;
import com.dailytask.service.EnergyAssessmentService;
import com.dailytask.service.ReflectionService;
import com.dailytask.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/users/{username}/daily-data")
@CrossOrigin(origins = "*")
public class DailyDataController {

    private final TaskService taskService;
    private final ReflectionService reflectionService;
    private final EnergyAssessmentService energyAssessmentService;

    @Autowired
    public DailyDataController(TaskService taskService, 
                              ReflectionService reflectionService, 
                              EnergyAssessmentService energyAssessmentService) {
        this.taskService = taskService;
        this.reflectionService = reflectionService;
        this.energyAssessmentService = energyAssessmentService;
    }

    @GetMapping
    public ResponseEntity<DailyDataResponse> getDailyData(
            @PathVariable String username,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        
        LocalDate queryDate = date != null ? date : LocalDate.now();
        
        List<TaskResponse> tasks = taskService.getTasksByUserAndDate(username, queryDate);
        ReflectionResponse reflection = reflectionService.getReflectionByUserAndDate(username, queryDate);
        EnergyAssessmentResponse energyAssessment = energyAssessmentService.getEnergyAssessmentByUserAndDate(username, queryDate);
        
        long totalTasks = tasks.size();
        long completedTasks = tasks.stream().mapToLong(task -> task.completed() ? 1 : 0).sum();
        long incompleteTasks = totalTasks - completedTasks;
        long starredTasks = tasks.stream().mapToLong(task -> task.starred() ? 1 : 0).sum();
        
        Double averageEnergyRating = reflection != null ? reflection.energyRating().doubleValue() : null;
        Double averageEnergyLevel = energyAssessment != null ? energyAssessment.energyLevel().doubleValue() : null;
        
        DailyDataResponse.DailyStats stats = new DailyDataResponse.DailyStats(
            totalTasks,
            completedTasks,
            incompleteTasks,
            starredTasks,
            averageEnergyRating,
            averageEnergyLevel
        );
        
        DailyDataResponse response = new DailyDataResponse(
            username,
            queryDate,
            tasks,
            reflection,
            energyAssessment,
            stats
        );
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/range")
    public ResponseEntity<List<DailyDataResponse>> getDailyDataRange(
            @PathVariable String username,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        
        List<DailyDataResponse> dailyDataList = startDate.datesUntil(endDate.plusDays(1))
            .map(date -> {
                List<TaskResponse> tasks = taskService.getTasksByUserAndDate(username, date);
                ReflectionResponse reflection = reflectionService.getReflectionByUserAndDate(username, date);
                EnergyAssessmentResponse energyAssessment = energyAssessmentService.getEnergyAssessmentByUserAndDate(username, date);
                
                long totalTasks = tasks.size();
                long completedTasks = tasks.stream().mapToLong(task -> task.completed() ? 1 : 0).sum();
                long incompleteTasks = totalTasks - completedTasks;
                long starredTasks = tasks.stream().mapToLong(task -> task.starred() ? 1 : 0).sum();
                
                Double averageEnergyRating = reflection != null ? reflection.energyRating().doubleValue() : null;
                Double averageEnergyLevel = energyAssessment != null ? energyAssessment.energyLevel().doubleValue() : null;
                
                DailyDataResponse.DailyStats stats = new DailyDataResponse.DailyStats(
                    totalTasks,
                    completedTasks,
                    incompleteTasks,
                    starredTasks,
                    averageEnergyRating,
                    averageEnergyLevel
                );
                
                return new DailyDataResponse(
                    username,
                    date,
                    tasks,
                    reflection,
                    energyAssessment,
                    stats
                );
            })
            .toList();
        
        return ResponseEntity.ok(dailyDataList);
    }
} 