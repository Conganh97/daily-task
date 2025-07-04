# Backend Project Structure - Daily Task App

## 🏗️ Technology Stack
- **Framework**: Spring Boot 3.5.3 | **Language**: Java 17 | **Database**: PostgreSQL
- **ORM**: JPA/Hibernate | **Build Tool**: Maven | **Additional**: Lombok, MapStruct

## 📁 Package Structure

```
com.dailytask/
├── config/          # CorsConfig, JpaConfig
├── controller/      # UserController, TaskController, ReflectionController, EnergyController
├── dto/
│   ├── request/     # UserCreateRequest, TaskCreateRequest, TaskUpdateRequest, ReflectionRequest, EnergyRequest
│   └── response/    # UserResponse, TaskResponse, ReflectionResponse, EnergyResponse, DailyDataResponse
├── entity/          # User, Task, Reflection, EnergyAssessment
├── exception/       # GlobalExceptionHandler
├── repository/      # UserRepository, TaskRepository, ReflectionRepository, EnergyAssessmentRepository
├── service/         # UserService, TaskService, ReflectionService, EnergyService
├── util/            # DateUtil, ValidationUtil
└── DailyTaskApplication.java
```

## 🗄️ Database Schema

**Users**: `id, username (unique), created_at, updated_at`

**Tasks**: `id, user_id (FK), title, description, completed, starred, date, created_at, updated_at`
- Indexes: `(user_id, date)`, `(user_id, starred)`, `(user_id, completed)`

**Reflections**: `id, user_id (FK), date, energy_rating (1-10), reflection_text, created_at, updated_at`
- Unique: `(user_id, date)`

**Energy Assessments**: `id, user_id (FK), date, energy_level (1-5), created_at, updated_at`
- Unique: `(user_id, date)`

## 🔧 Key Components

**Entity Relationships**: User (1) → (N) Task/Reflection/EnergyAssessment

**Services**: UserService, TaskService, ReflectionService, EnergyService

**Controllers**: RESTful API with user-specific endpoints, proper HTTP status codes, input validation

**Repositories**: JPA repositories with custom query methods for date-based filtering

## 📊 API Endpoints

**Users**: `GET /api/users`, `POST /api/users`, `GET /api/users/{username}/exists`

**Tasks**: `GET|POST /api/users/{username}/tasks`, `PUT|DELETE /api/users/{username}/tasks/{id}`
- `PATCH /api/users/{username}/tasks/{id}/complete|star`

**Reflections**: `GET|POST /api/users/{username}/reflections`, `PUT|DELETE /api/users/{username}/reflections/{id}`

**Energy**: `GET|POST /api/users/{username}/energy`, `PUT /api/users/{username}/energy/{id}`

**Combined**: `GET /api/users/{username}/daily-data?date={date}`

## 🎯 Key Features

**Clean Architecture**: Separation of concerns, dependency injection, single responsibility principle

**Data Integrity**: Foreign key relationships, unique constraints, proper validation

**Performance**: Database indexing, lazy loading, efficient queries

**User Management**: Username-only identification, data isolation per user, no authentication

**Exception Handling**: Single `GlobalExceptionHandler` for all exceptions with consistent HTTP status codes

## 📝 Configuration

**Database**: PostgreSQL connection, JPA/Hibernate settings, SQL logging
**Application**: Port 8080, CORS for frontend integration
**Development**: Dev/prod profiles, Flyway migration 