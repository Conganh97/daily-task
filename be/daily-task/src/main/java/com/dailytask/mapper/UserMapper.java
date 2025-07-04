package com.dailytask.mapper;

import com.dailytask.dto.request.CreateUserRequest;
import com.dailytask.dto.response.UserResponse;
import com.dailytask.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserResponse toResponse(User user);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "tasks", ignore = true)
    @Mapping(target = "reflections", ignore = true)
    @Mapping(target = "energyAssessments", ignore = true)
    User toEntity(CreateUserRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "tasks", ignore = true)
    @Mapping(target = "reflections", ignore = true)
    @Mapping(target = "energyAssessments", ignore = true)
    void updateEntity(CreateUserRequest request, @MappingTarget User user);
} 