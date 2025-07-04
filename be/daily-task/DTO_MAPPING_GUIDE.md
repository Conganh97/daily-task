# DTO Mapping Guide - Daily Task API

## Overview

This document describes the Data Transfer Objects (DTOs) and their mappings implemented using MapStruct in the Daily Task application.

## Architecture

The application uses a clean separation between entities and DTOs:
- **Entities**: Database-mapped domain objects
- **Request DTOs**: Input validation and data transfer for API requests
- **Response DTOs**: Output data transfer for API responses
- **Mappers**: MapStruct interfaces for automatic mapping between entities and DTOs

## Request DTOs

### User Management
- **CreateUserRequest**: Used for creating new users
  - Fields: `username` (3-50 chars, required)
  - Validation: NotBlank, Size constraints

### Task Management
- **CreateTaskRequest**: Used for creating new tasks
  - Fields: `title` (required, max 200 chars), `description` (max 1000 chars), `date` (required), `username` (required)
  - Validation: NotBlank for title/username, NotNull for date, Size constraints

- **UpdateTaskRequest**: Used for partial task updates
  - Fields: `title` (max 200 chars), `description` (max 1000 chars), `completed`, `starred`
  - Validation: Size constraints, all fields optional

### Reflection Management
- **CreateReflectionRequest**: Used for creating/updating reflections
  - Fields: `date` (required), `energyRating` (1-10, required), `reflectionText` (max 2000 chars), `username` (required)
  - Validation: NotNull for date/energyRating, Min/Max for rating, Size constraints

- **UpdateReflectionRequest**: Used for partial reflection updates
  - Fields: `energyRating` (1-10), `reflectionText` (max 2000 chars)
  - Validation: Min/Max for rating, Size constraints, all fields optional

### Energy Assessment Management
- **CreateEnergyAssessmentRequest**: Used for creating/updating energy assessments
  - Fields: `date` (required), `energyLevel` (1-5, required), `username` (required)
  - Validation: NotNull for date/energyLevel, Min/Max for level

- **UpdateEnergyAssessmentRequest**: Used for partial energy assessment updates
  - Fields: `energyLevel` (1-5)
  - Validation: Min/Max for level, optional field

## Response DTOs

### User Management
- **UserResponse**: User information for API responses
  - Fields: `id`, `username`, `createdAt`, `updatedAt`

### Task Management
- **TaskResponse**: Task information for API responses
  - Fields: `id`, `title`, `description`, `date`, `completed`, `starred`, `username`, `createdAt`, `updatedAt`

### Reflection Management
- **ReflectionResponse**: Reflection information for API responses
  - Fields: `id`, `date`, `energyRating`, `reflectionText`, `username`, `createdAt`, `updatedAt`

### Energy Assessment Management
- **EnergyAssessmentResponse**: Energy assessment information for API responses
  - Fields: `id`, `date`, `energyLevel`, `username`, `createdAt`, `updatedAt`

### Combined Data
- **DailyDataResponse**: Combined daily data for API responses
  - Fields: `username`, `date`, `tasks`, `reflection`, `energyAssessment`, `stats`
  - Nested: `DailyStats` with task counts and averages

## MapStruct Mappers

### UserMapper
- **toResponse(User)**: Entity → Response DTO
- **toEntity(CreateUserRequest)**: Request DTO → Entity (ignores collections)
- **updateEntity(CreateUserRequest, User)**: Updates existing entity

### TaskMapper
- **toResponse(Task)**: Entity → Response DTO (maps user.username)
- **toEntity(CreateTaskRequest)**: Request DTO → Entity (sets defaults)
- **updateEntity(UpdateTaskRequest, Task)**: Partial update with null-value strategy

### ReflectionMapper
- **toResponse(Reflection)**: Entity → Response DTO (maps user.username)
- **toEntity(CreateReflectionRequest)**: Request DTO → Entity
- **updateEntity(CreateReflectionRequest, Reflection)**: Updates existing entity

### EnergyAssessmentMapper
- **toResponse(EnergyAssessment)**: Entity → Response DTO (maps user.username)
- **toEntity(CreateEnergyAssessmentRequest)**: Request DTO → Entity
- **updateEntity(CreateEnergyAssessmentRequest, EnergyAssessment)**: Updates existing entity

## Mapping Configuration

### MapStruct Settings
- **componentModel**: "spring" - Generates Spring beans
- **nullValuePropertyMappingStrategy**: IGNORE for update operations
- **@Mapping annotations**: Used to ignore auto-generated fields and relationships

### Key Mapping Rules
1. **ID Generation**: Always ignored in create/update operations
2. **Timestamps**: createdAt/updatedAt ignored in mapping (handled by JPA)
3. **User Relationships**: Set manually in service layer after mapping
4. **Collections**: Ignored in User entity mapping to avoid circular references
5. **Null Values**: Ignored in update operations to preserve existing data

## Validation Features

### Bean Validation Annotations
- **@NotNull**: Required fields that cannot be null
- **@NotBlank**: String fields that cannot be null, empty, or whitespace
- **@Size**: String length constraints
- **@Min/@Max**: Numeric range constraints

### Custom Validation
- **Username**: 3-50 characters, alphanumeric
- **Energy Rating**: 1-10 scale for reflections
- **Energy Level**: 1-5 scale for assessments
- **Text Fields**: Reasonable length limits to prevent abuse

### Controller Integration
- **@Valid**: Used in controller methods to trigger validation
- **ResponseEntity**: Proper HTTP status codes for validation errors
- **Global Exception Handler**: Centralized validation error handling

## Benefits of This Architecture

1. **Type Safety**: Compile-time checking of DTO mappings
2. **Performance**: Generated code is optimized and fast
3. **Maintainability**: Clear separation of concerns
4. **Validation**: Comprehensive input validation at DTO level
5. **Documentation**: Self-documenting code with clear DTOs
6. **Flexibility**: Easy to add new fields or modify mappings

## Usage Examples

### Creating a Task
```java
CreateTaskRequest request = new CreateTaskRequest(
    "Complete project",
    "Finish the daily task application",
    LocalDate.now(),
    "john.doe"
);
// Automatically mapped to Task entity with validation
```

### Updating a Task
```java
UpdateTaskRequest request = new UpdateTaskRequest(
    null,           // Keep existing title
    "Updated desc", // Update description
    true,           // Mark as completed
    null            // Keep existing starred status
);
// Only non-null fields are updated
```

This architecture provides a robust, maintainable, and well-validated API layer for the Daily Task application. 