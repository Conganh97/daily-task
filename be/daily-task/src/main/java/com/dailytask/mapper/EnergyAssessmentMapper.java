package com.dailytask.mapper;

import com.dailytask.dto.request.CreateEnergyAssessmentRequest;
import com.dailytask.dto.response.EnergyAssessmentResponse;
import com.dailytask.entity.EnergyAssessment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface EnergyAssessmentMapper {

    @Mapping(source = "user.username", target = "username")
    EnergyAssessmentResponse toResponse(EnergyAssessment energyAssessment);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "user", ignore = true)
    EnergyAssessment toEntity(CreateEnergyAssessmentRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "date", ignore = true)
    void updateEntity(CreateEnergyAssessmentRequest request, @MappingTarget EnergyAssessment energyAssessment);
} 