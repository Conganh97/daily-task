package com.dailytask.service;

import com.dailytask.dto.request.CreateTaskRequest;
import com.dailytask.dto.request.UpdateTaskRequest;
import com.dailytask.dto.response.TaskResponse;

import java.time.LocalDate;
import java.util.List;

public interface TaskService {
    TaskResponse createTask(CreateTaskRequest request);
    TaskResponse updateTask(Long id, UpdateTaskRequest request);
    TaskResponse getTaskById(Long id);
    List<TaskResponse> getTasksByUserAndDate(String username, LocalDate date);
    List<TaskResponse> getTasksByUserAndDateRange(String username, LocalDate startDate, LocalDate endDate);
    List<TaskResponse> getStarredTasksByUser(String username);
    List<TaskResponse> getIncompleteTasksByUser(String username);
    List<TaskResponse> getAllTasksByUser(String username);
    void deleteTask(Long id);
    long getTaskCountByUserAndDate(String username, LocalDate date);
    long getCompletedTaskCountByUserAndDate(String username, LocalDate date);
    long getStarredTaskCountByUser(String username);
} 