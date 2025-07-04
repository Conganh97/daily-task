package com.dailytask.mapper;

import com.dailytask.dto.request.CreateReflectionRequest;
import com.dailytask.dto.response.ReflectionResponse;
import com.dailytask.entity.Reflection;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ReflectionMapper {

    @Mapping(source = "user.username", target = "username")
    ReflectionResponse toResponse(Reflection reflection);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "user", ignore = true)
    Reflection toEntity(CreateReflectionRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "date", ignore = true)
    void updateEntity(CreateReflectionRequest request, @MappingTarget Reflection reflection);
} 