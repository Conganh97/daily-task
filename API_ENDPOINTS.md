# Daily Task API Endpoints

## User Management Endpoints

### UserController (`/api/users`)
- `GET /api/users` - Get all users
- `POST /api/users` - Create a new user
- `GET /api/users/{username}/exists` - Check if username exists
- `GET /api/users/{username}` - Get user by username
- `GET /api/users/search?searchTerm={term}` - Search users
- `DELETE /api/users/{id}` - Delete user by ID

## Task Management Endpoints

### TaskController (`/api/users/{username}/tasks`)
- `GET /api/users/{username}/tasks` - Get tasks (supports filtering by date, date range, starred, incomplete)
- `POST /api/users/{username}/tasks` - Create a new task
- `PUT /api/users/{username}/tasks/{id}` - Update task
- `DELETE /api/users/{username}/tasks/{id}` - Delete task
- `GET /api/users/{username}/tasks/{id}` - Get task by ID
- `PATCH /api/users/{username}/tasks/{id}/complete` - Toggle task completion
- `PATCH /api/users/{username}/tasks/{id}/star` - Toggle task star
- `GET /api/users/{username}/tasks/stats` - Get task statistics

Query Parameters for GET tasks:
- `date` - Filter by specific date (ISO format: YYYY-MM-DD)
- `startDate` & `endDate` - Filter by date range
- `starred=true` - Get only starred tasks
- `incomplete=true` - Get only incomplete tasks

## Reflection Management Endpoints

### ReflectionController (`/api/users/{username}/reflections`)
- `GET /api/users/{username}/reflections` - Get reflections (supports filtering)
- `POST /api/users/{username}/reflections` - Create or update reflection
- `PUT /api/users/{username}/reflections/{id}` - Update reflection
- `DELETE /api/users/{username}/reflections/{id}` - Delete reflection
- `GET /api/users/{username}/reflections/{id}` - Get reflection by ID
- `GET /api/users/{username}/reflections/today` - Get today's reflection
- `GET /api/users/{username}/reflections/exists?date={date}` - Check if reflection exists for date
- `GET /api/users/{username}/reflections/average-rating` - Get average energy rating

Query Parameters for GET reflections:
- `date` - Filter by specific date
- `startDate` & `endDate` - Filter by date range
- `minRating` & `maxRating` - Filter by energy rating range

## Energy Assessment Endpoints

### EnergyAssessmentController (`/api/users/{username}/energy`)
- `GET /api/users/{username}/energy` - Get energy assessments (supports filtering)
- `POST /api/users/{username}/energy` - Create or update energy assessment
- `PUT /api/users/{username}/energy/{id}` - Update energy assessment
- `DELETE /api/users/{username}/energy/{id}` - Delete energy assessment
- `GET /api/users/{username}/energy/{id}` - Get energy assessment by ID
- `GET /api/users/{username}/energy/today` - Get today's energy assessment
- `GET /api/users/{username}/energy/exists?date={date}` - Check if energy assessment exists for date
- `GET /api/users/{username}/energy/average-level` - Get average energy level

Query Parameters for GET energy assessments:
- `date` - Filter by specific date
- `startDate` & `endDate` - Filter by date range
- `minLevel` & `maxLevel` - Filter by energy level range

## Combined Data Endpoints

### DailyDataController (`/api/users/{username}/daily-data`)
- `GET /api/users/{username}/daily-data` - Get all daily data for user
- `GET /api/users/{username}/daily-data/range` - Get daily data for date range

Query Parameters:
- `date` - Specific date (defaults to today)
- `startDate` & `endDate` - Date range (for /range endpoint)

### Daily Data Response Structure
```json
{
  "username": "string",
  "date": "2024-01-01",
  "tasks": [/* array of task objects */],
  "reflection": {/* reflection object or null */},
  "energyAssessment": {/* energy assessment object or null */},
  "stats": {
    "totalTasks": 0,
    "completedTasks": 0,
    "incompleteTasks": 0,
    "starredTasks": 0,
    "averageEnergyRating": 0.0,
    "averageEnergyLevel": 0.0
  }
}
```

## Health Check

### HealthController (`/api/health`)
- `GET /api/health` - Application health check

## Common HTTP Status Codes

- `200 OK` - Successful GET requests
- `201 Created` - Successful POST requests
- `204 No Content` - Successful DELETE requests
- `400 Bad Request` - Invalid request data
- `404 Not Found` - Resource not found
- `409 Conflict` - Duplicate resource (e.g., username already exists)
- `500 Internal Server Error` - Server error

## Date Format

All date parameters and responses use ISO 8601 format: `YYYY-MM-DD` (e.g., `2024-01-01`)

## CORS Configuration

All endpoints support CORS with `origins = "*"` for development purposes. 