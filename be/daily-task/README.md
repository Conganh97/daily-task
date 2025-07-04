# Daily Task API

## Overview
Backend REST API for Daily Task application built with Spring Boot, PostgreSQL, and JPA/Hibernate.

## Technology Stack
- **Framework**: Spring Boot 3.5.3
- **Language**: Java 17
- **Database**: PostgreSQL
- **ORM**: JPA/Hibernate
- **Build Tool**: Maven
- **Additional**: Lombok, MapStruct, Flyway

## Prerequisites
- Java 17 or higher
- Maven 3.6+
- PostgreSQL 12+

## Setup Instructions

### 1. Database Setup
Create PostgreSQL databases:
```sql
CREATE DATABASE daily_task;
CREATE DATABASE daily_task_dev;
```

### 2. Environment Variables
Set the following environment variables:
```bash
export DB_USERNAME=postgres
export DB_PASSWORD=your_password
```

### 3. Run the Application
```bash
# Development mode
mvn spring-boot:run -Dspring-boot.run.profiles=dev

# Production mode  
mvn spring-boot:run -Dspring-boot.run.profiles=prod
```

### 4. API Documentation
- Swagger UI: http://localhost:8080/swagger-ui.html
- API Docs: http://localhost:8080/v3/api-docs

### 5. Health Check
Test the application: http://localhost:8080/api/health

### 6. Running Tests
```bash
# Run all tests
mvn test

# Run only repository tests
mvn test -Dtest="*RepositoryTest"

# Run only service tests  
mvn test -Dtest="*ServiceTest"
```

## Project Structure
```
com.dailytask/
├── config/          # Configuration classes
├── controller/      # REST controllers
├── dto/            # Data Transfer Objects
│   ├── request/     # Request DTOs with validation
│   └── response/    # Response DTOs
├── entity/         # JPA entities
├── exception/      # Exception handling & global error handler
├── repository/     # JPA repositories with custom queries
├── service/        # Business logic interfaces
│   └── impl/       # Service implementations
└── util/           # Utility classes
```

## Key Features
- **User Management**: Username-based user system without authentication
- **Task Management**: CRUD operations with date filtering, completion tracking, and starring
- **Reflection System**: Daily reflections with energy ratings (1-10) and text
- **Energy Assessment**: Quick daily energy level tracking (1-5)
- **Analytics**: Average energy calculations and statistical queries
- **Data Integrity**: One reflection and one energy assessment per user per date (upsert operations)

## Development Notes
- Use `dev` profile for development
- Use `prod` profile for production
- Database migrations are handled by Flyway
- CORS is configured for frontend integration
- Logging is configured for debugging
- Service layer implements proper transaction management
- DTOs include comprehensive validation annotations
- Global exception handling for consistent error responses
- Upsert operations for Reflections and Energy Assessments (one per user per date)

## Phase 2 Completed ✅
- ✅ Database schema design with Flyway migrations
- ✅ JPA entities with proper relationships and validation
- ✅ Sample data for testing
- ✅ Comprehensive database documentation

## Phase 3 Completed ✅
- ✅ JPA repositories with custom query methods
- ✅ UserRepository with username operations  
- ✅ TaskRepository with date-based filtering and completion tracking
- ✅ ReflectionRepository with energy rating queries and analytics
- ✅ EnergyAssessmentRepository with statistical queries
- ✅ Comprehensive repository unit tests
- ✅ H2 test database configuration for unit testing

## Phase 4 Completed ✅ (Service Layer)
- ✅ Request and Response DTOs with validation annotations
  - CreateUserRequest, CreateTaskRequest, UpdateTaskRequest
  - CreateReflectionRequest, CreateEnergyAssessmentRequest
  - UserResponse, TaskResponse, ReflectionResponse, EnergyAssessmentResponse
- ✅ Custom exception classes
  - ResourceNotFoundException, DuplicateResourceException
  - GlobalExceptionHandler with centralized error handling
- ✅ Service interfaces and implementations
  - UserService: CRUD operations, username validation, search functionality
  - TaskService: Task management, date filtering, completion tracking
  - ReflectionService: Upsert operations, energy rating analytics
  - EnergyAssessmentService: Upsert operations, statistical queries
- ✅ Business logic with proper transaction management
- ✅ Service unit tests with mocked dependencies
- ✅ Entity-to-DTO mapping methods

## Next Steps
- Phase 5: REST API controllers and OpenAPI documentation

## Implementation Summary
### Database Layer
- 5 Flyway migration files (V1-V5)
- 4 JPA entities with relationships and validation
- 4 Repository interfaces with custom queries
- Sample data for testing

### Service Layer  
- 5 Request DTOs with validation annotations
- 4 Response DTOs for API responses
- 2 Custom exception classes + GlobalExceptionHandler
- 4 Service interfaces with 4 implementation classes
- Comprehensive unit tests for service layer

### Configuration & Infrastructure
- Multi-environment configuration (dev, prod, test)
- H2 in-memory database for testing
- CORS configuration for frontend integration
- Transaction management and error handling

## Database Information
- See `DATABASE_SCHEMA.md` for complete database documentation
- All tables include audit fields (createdAt, updatedAt)
- Unique constraints ensure data integrity 