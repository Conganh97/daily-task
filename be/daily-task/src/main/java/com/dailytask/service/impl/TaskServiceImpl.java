package com.dailytask.service.impl;

import com.dailytask.dto.request.CreateTaskRequest;
import com.dailytask.dto.request.UpdateTaskRequest;
import com.dailytask.dto.response.TaskResponse;
import com.dailytask.entity.Task;
import com.dailytask.entity.User;
import com.dailytask.exception.ResourceNotFoundException;
import com.dailytask.mapper.TaskMapper;
import com.dailytask.repository.TaskRepository;
import com.dailytask.repository.UserRepository;
import com.dailytask.service.TaskService;
import com.dailytask.service.validation.BusinessValidationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final TaskMapper taskMapper;
    private final BusinessValidationService businessValidationService;

    public TaskServiceImpl(TaskRepository taskRepository, UserRepository userRepository, 
                          TaskMapper taskMapper, BusinessValidationService businessValidationService) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.taskMapper = taskMapper;
        this.businessValidationService = businessValidationService;
    }

    @Override
    public TaskResponse createTask(CreateTaskRequest request) {
        User user = userRepository.findByUsername(request.username())
            .orElseThrow(() -> new ResourceNotFoundException("User", "username", request.username()));

        // Apply business validation
        businessValidationService.validateTaskCreation(user, request.date());

        Task task = taskMapper.toEntity(request);
        task.setUser(user);

        Task savedTask = taskRepository.save(task);
        return taskMapper.toResponse(savedTask);
    }

    @Override
    public TaskResponse updateTask(Long id, UpdateTaskRequest request) {
        Task task = taskRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Task", "id", id));

        taskMapper.updateEntity(request, task);

        Task savedTask = taskRepository.save(task);
        return taskMapper.toResponse(savedTask);
    }

    @Override
    @Transactional(readOnly = true)
    public TaskResponse getTaskById(Long id) {
        Task task = taskRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Task", "id", id));
        return taskMapper.toResponse(task);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaskResponse> getTasksByUserAndDate(String username, LocalDate date) {
        User user = getUserByUsername(username);
        return taskRepository.findByUserAndDateOrderByStarredDescCreatedAtAsc(user, date).stream()
            .map(taskMapper::toResponse)
            .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaskResponse> getTasksByUserAndDateRange(String username, LocalDate startDate, LocalDate endDate) {
        User user = getUserByUsername(username);
        
        // Validate date range
        businessValidationService.validateDateRange(startDate, endDate);
        
        return taskRepository.findByUserAndDateBetweenOrderByDateDescStarredDesc(user, startDate, endDate).stream()
            .map(taskMapper::toResponse)
            .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaskResponse> getStarredTasksByUser(String username) {
        User user = getUserByUsername(username);
        return taskRepository.findStarredTasksByUserOrderByDateDesc(user).stream()
            .map(taskMapper::toResponse)
            .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaskResponse> getIncompleteTasksByUser(String username) {
        User user = getUserByUsername(username);
        return taskRepository.findIncompleteTasksByUserOrderByDateAscStarredDesc(user).stream()
            .map(taskMapper::toResponse)
            .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaskResponse> getAllTasksByUser(String username) {
        User user = getUserByUsername(username);
        return taskRepository.findByUserOrderByDateDescCreatedAtDesc(user).stream()
            .map(taskMapper::toResponse)
            .toList();
    }

    @Override
    public void deleteTask(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new ResourceNotFoundException("Task", "id", id);
        }
        taskRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public long getTaskCountByUserAndDate(String username, LocalDate date) {
        User user = getUserByUsername(username);
        return taskRepository.countByUserAndDate(user, date);
    }

    @Override
    @Transactional(readOnly = true)
    public long getCompletedTaskCountByUserAndDate(String username, LocalDate date) {
        User user = getUserByUsername(username);
        return taskRepository.countCompletedByUserAndDate(user, date);
    }

    @Override
    @Transactional(readOnly = true)
    public long getStarredTaskCountByUser(String username) {
        User user = getUserByUsername(username);
        return taskRepository.countStarredByUser(user);
    }

    private User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
            .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));
    }


} 