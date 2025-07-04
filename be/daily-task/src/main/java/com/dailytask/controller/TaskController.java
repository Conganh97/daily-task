package com.dailytask.controller;

import com.dailytask.dto.request.CreateTaskRequest;
import com.dailytask.dto.request.UpdateTaskRequest;
import com.dailytask.dto.response.TaskResponse;
import com.dailytask.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/users/{username}/tasks")
@CrossOrigin(origins = "*")
public class TaskController {

    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public ResponseEntity<List<TaskResponse>> getTasks(
            @PathVariable String username,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) Boolean starred,
            @RequestParam(required = false) Boolean incomplete) {
        
        List<TaskResponse> tasks;
        
        if (date != null) {
            tasks = taskService.getTasksByUserAndDate(username, date);
        } else if (startDate != null && endDate != null) {
            tasks = taskService.getTasksByUserAndDateRange(username, startDate, endDate);
        } else if (Boolean.TRUE.equals(starred)) {
            tasks = taskService.getStarredTasksByUser(username);
        } else if (Boolean.TRUE.equals(incomplete)) {
            tasks = taskService.getIncompleteTasksByUser(username);
        } else {
            tasks = taskService.getAllTasksByUser(username);
        }
        
        return ResponseEntity.ok(tasks);
    }

    @PostMapping
    public ResponseEntity<TaskResponse> createTask(
            @PathVariable String username,
            @Valid @RequestBody CreateTaskRequest request) {
        TaskResponse createdTask = taskService.createTask(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTask);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskResponse> updateTask(
            @PathVariable String username,
            @PathVariable Long id,
            @Valid @RequestBody UpdateTaskRequest request) {
        TaskResponse updatedTask = taskService.updateTask(id, request);
        return ResponseEntity.ok(updatedTask);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(
            @PathVariable String username,
            @PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponse> getTaskById(
            @PathVariable String username,
            @PathVariable Long id) {
        TaskResponse task = taskService.getTaskById(id);
        return ResponseEntity.ok(task);
    }

    @PatchMapping("/{id}/complete")
    public ResponseEntity<TaskResponse> toggleTaskCompletion(
            @PathVariable String username,
            @PathVariable Long id) {
        TaskResponse task = taskService.getTaskById(id);
        UpdateTaskRequest request = new UpdateTaskRequest(
            null, 
            null, 
            !task.completed(), 
            null
        );
        TaskResponse updatedTask = taskService.updateTask(id, request);
        return ResponseEntity.ok(updatedTask);
    }

    @PatchMapping("/{id}/star")
    public ResponseEntity<TaskResponse> toggleTaskStar(
            @PathVariable String username,
            @PathVariable Long id) {
        TaskResponse task = taskService.getTaskById(id);
        UpdateTaskRequest request = new UpdateTaskRequest(
            null, 
            null, 
            null, 
            !task.starred()
        );
        TaskResponse updatedTask = taskService.updateTask(id, request);
        return ResponseEntity.ok(updatedTask);
    }

    @GetMapping("/stats")
    public ResponseEntity<TaskStatsResponse> getTaskStats(
            @PathVariable String username,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        
        LocalDate queryDate = date != null ? date : LocalDate.now();
        
        long totalTasks = taskService.getTaskCountByUserAndDate(username, queryDate);
        long completedTasks = taskService.getCompletedTaskCountByUserAndDate(username, queryDate);
        long starredTasks = taskService.getStarredTaskCountByUser(username);
        
        TaskStatsResponse stats = new TaskStatsResponse(
            totalTasks,
            completedTasks,
            starredTasks,
            totalTasks - completedTasks
        );
        
        return ResponseEntity.ok(stats);
    }

    public record TaskStatsResponse(
        long totalTasks,
        long completedTasks,
        long starredTasks,
        long incompleteTasks
    ) {}
} 