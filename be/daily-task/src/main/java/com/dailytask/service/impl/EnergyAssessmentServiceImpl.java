package com.dailytask.service.impl;

import com.dailytask.dto.request.CreateEnergyAssessmentRequest;
import com.dailytask.dto.response.EnergyAssessmentResponse;
import com.dailytask.entity.EnergyAssessment;
import com.dailytask.entity.User;
import com.dailytask.exception.ResourceNotFoundException;
import com.dailytask.mapper.EnergyAssessmentMapper;
import com.dailytask.repository.EnergyAssessmentRepository;
import com.dailytask.repository.UserRepository;
import com.dailytask.service.EnergyAssessmentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class EnergyAssessmentServiceImpl implements EnergyAssessmentService {

    private final EnergyAssessmentRepository energyAssessmentRepository;
    private final UserRepository userRepository;
    private final EnergyAssessmentMapper energyAssessmentMapper;

    public EnergyAssessmentServiceImpl(EnergyAssessmentRepository energyAssessmentRepository, UserRepository userRepository, EnergyAssessmentMapper energyAssessmentMapper) {
        this.energyAssessmentRepository = energyAssessmentRepository;
        this.userRepository = userRepository;
        this.energyAssessmentMapper = energyAssessmentMapper;
    }

    @Override
    public EnergyAssessmentResponse createOrUpdateEnergyAssessment(CreateEnergyAssessmentRequest request) {
        User user = getUserByUsername(request.username());
        
        Optional<EnergyAssessment> existingAssessment = energyAssessmentRepository.findByUserAndDate(user, request.date());
        
        EnergyAssessment assessment;
        if (existingAssessment.isPresent()) {
            assessment = existingAssessment.get();
            energyAssessmentMapper.updateEntity(request, assessment);
        } else {
            assessment = energyAssessmentMapper.toEntity(request);
            assessment.setUser(user);
        }

        EnergyAssessment savedAssessment = energyAssessmentRepository.save(assessment);
        return energyAssessmentMapper.toResponse(savedAssessment);
    }

    @Override
    @Transactional(readOnly = true)
    public EnergyAssessmentResponse getEnergyAssessmentById(Long id) {
        EnergyAssessment assessment = energyAssessmentRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("EnergyAssessment", "id", id));
        return energyAssessmentMapper.toResponse(assessment);
    }

    @Override
    @Transactional(readOnly = true)
    public EnergyAssessmentResponse getEnergyAssessmentByUserAndDate(String username, LocalDate date) {
        User user = getUserByUsername(username);
        EnergyAssessment assessment = energyAssessmentRepository.findByUserAndDate(user, date)
            .orElseThrow(() -> new ResourceNotFoundException(
                "EnergyAssessment not found for user '" + username + "' on date '" + date + "'"));
        return energyAssessmentMapper.toResponse(assessment);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EnergyAssessmentResponse> getEnergyAssessmentsByUserAndDateRange(String username, LocalDate startDate, LocalDate endDate) {
        User user = getUserByUsername(username);
        return energyAssessmentRepository.findByUserAndDateBetweenOrderByDateDesc(user, startDate, endDate).stream()
            .map(energyAssessmentMapper::toResponse)
            .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<EnergyAssessmentResponse> getAllEnergyAssessmentsByUser(String username) {
        User user = getUserByUsername(username);
        return energyAssessmentRepository.findByUserOrderByDateDesc(user).stream()
            .map(energyAssessmentMapper::toResponse)
            .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<EnergyAssessmentResponse> getEnergyAssessmentsByEnergyLevel(String username, Integer minLevel, Integer maxLevel) {
        User user = getUserByUsername(username);
        return energyAssessmentRepository.findByUserAndEnergyLevelBetweenOrderByDateDesc(user, minLevel, maxLevel).stream()
            .map(energyAssessmentMapper::toResponse)
            .toList();
    }

    @Override
    public void deleteEnergyAssessment(Long id) {
        if (!energyAssessmentRepository.existsById(id)) {
            throw new ResourceNotFoundException("EnergyAssessment", "id", id);
        }
        energyAssessmentRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsEnergyAssessmentByUserAndDate(String username, LocalDate date) {
        User user = getUserByUsername(username);
        return energyAssessmentRepository.existsByUserAndDate(user, date);
    }

    @Override
    @Transactional(readOnly = true)
    public Double getAverageEnergyLevelByUser(String username) {
        User user = getUserByUsername(username);
        Double average = energyAssessmentRepository.findAverageEnergyLevelByUser(user);
        return average != null ? average : 0.0;
    }

    @Override
    @Transactional(readOnly = true)
    public Double getAverageEnergyLevelByUserAndDateRange(String username, LocalDate startDate, LocalDate endDate) {
        User user = getUserByUsername(username);
        Double average = energyAssessmentRepository.findAverageEnergyLevelByUserAndDateBetween(user, startDate, endDate);
        return average != null ? average : 0.0;
    }

    private User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
            .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));
    }


} 