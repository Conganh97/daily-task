package com.dailytask.service.impl;

import com.dailytask.dto.request.CreateReflectionRequest;
import com.dailytask.dto.response.ReflectionResponse;
import com.dailytask.entity.Reflection;
import com.dailytask.entity.User;
import com.dailytask.exception.ResourceNotFoundException;
import com.dailytask.mapper.ReflectionMapper;
import com.dailytask.repository.ReflectionRepository;
import com.dailytask.repository.UserRepository;
import com.dailytask.service.ReflectionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ReflectionServiceImpl implements ReflectionService {

    private final ReflectionRepository reflectionRepository;
    private final UserRepository userRepository;
    private final ReflectionMapper reflectionMapper;

    public ReflectionServiceImpl(ReflectionRepository reflectionRepository, UserRepository userRepository, ReflectionMapper reflectionMapper) {
        this.reflectionRepository = reflectionRepository;
        this.userRepository = userRepository;
        this.reflectionMapper = reflectionMapper;
    }

    @Override
    public ReflectionResponse createOrUpdateReflection(CreateReflectionRequest request) {
        User user = getUserByUsername(request.username());
        
        Optional<Reflection> existingReflection = reflectionRepository.findByUserAndDate(user, request.date());
        
        Reflection reflection;
        if (existingReflection.isPresent()) {
            reflection = existingReflection.get();
            reflectionMapper.updateEntity(request, reflection);
        } else {
            reflection = reflectionMapper.toEntity(request);
            reflection.setUser(user);
        }

        Reflection savedReflection = reflectionRepository.save(reflection);
        return reflectionMapper.toResponse(savedReflection);
    }

    @Override
    @Transactional(readOnly = true)
    public ReflectionResponse getReflectionById(Long id) {
        Reflection reflection = reflectionRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Reflection", "id", id));
        return reflectionMapper.toResponse(reflection);
    }

    @Override
    @Transactional(readOnly = true)
    public ReflectionResponse getReflectionByUserAndDate(String username, LocalDate date) {
        User user = getUserByUsername(username);
        Reflection reflection = reflectionRepository.findByUserAndDate(user, date)
            .orElseThrow(() -> new ResourceNotFoundException(
                "Reflection not found for user '" + username + "' on date '" + date + "'"));
        return reflectionMapper.toResponse(reflection);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReflectionResponse> getReflectionsByUserAndDateRange(String username, LocalDate startDate, LocalDate endDate) {
        User user = getUserByUsername(username);
        return reflectionRepository.findByUserAndDateBetweenOrderByDateDesc(user, startDate, endDate).stream()
            .map(reflectionMapper::toResponse)
            .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReflectionResponse> getAllReflectionsByUser(String username) {
        User user = getUserByUsername(username);
        return reflectionRepository.findByUserOrderByDateDesc(user).stream()
            .map(reflectionMapper::toResponse)
            .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReflectionResponse> getReflectionsByEnergyRating(String username, Integer minRating, Integer maxRating) {
        User user = getUserByUsername(username);
        return reflectionRepository.findByUserAndEnergyRatingBetweenOrderByDateDesc(user, minRating, maxRating).stream()
            .map(reflectionMapper::toResponse)
            .toList();
    }

    @Override
    public void deleteReflection(Long id) {
        if (!reflectionRepository.existsById(id)) {
            throw new ResourceNotFoundException("Reflection", "id", id);
        }
        reflectionRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsReflectionByUserAndDate(String username, LocalDate date) {
        User user = getUserByUsername(username);
        return reflectionRepository.existsByUserAndDate(user, date);
    }

    @Override
    @Transactional(readOnly = true)
    public Double getAverageEnergyRatingByUser(String username) {
        User user = getUserByUsername(username);
        Double average = reflectionRepository.findAverageEnergyRatingByUser(user);
        return average != null ? average : 0.0;
    }

    @Override
    @Transactional(readOnly = true)
    public Double getAverageEnergyRatingByUserAndDateRange(String username, LocalDate startDate, LocalDate endDate) {
        User user = getUserByUsername(username);
        Double average = reflectionRepository.findAverageEnergyRatingByUserAndDateBetween(user, startDate, endDate);
        return average != null ? average : 0.0;
    }

    private User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
            .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));
    }


} 