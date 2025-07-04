# Daily Task API Documentation for Frontend Integration

## 📋 Overview

**Base URL**: `http://localhost:8080/api`  
**Authentication**: Username-based (no tokens required)  
**Content-Type**: `application/json`

## 🔐 Authentication

Simple username-based system. Include username in URL path or request body as specified.

## 📚 API Endpoints

### 1. User Management

#### Get All Users
```http
GET /api/users
```

**Response:**
```json
[
  {
    "id": 1,
    "username": "john.doe",
    "createdAt": "2024-01-01T10:00:00",
    "updatedAt": "2024-01-01T10:00:00"
  }
]
```

#### Create User
```http
POST /api/users
```

**Request Body:**
```json
{
  "username": "john.doe"
}
```

**Validation Rules:**
- Username: 3-50 characters, letters/numbers/dots/hyphens/underscores only
- Cannot start/end with special characters
- No consecutive special characters
- Reserved usernames blocked (admin, root, system, etc.)

#### Check Username Exists
```http
GET /api/users/{username}/exists
```

#### Get User by Username
```http
GET /api/users/{username}
```

#### Search Users
```http
GET /api/users/search?searchTerm=john
```

#### Delete User
```http
DELETE /api/users/{id}
```

### 2. Task Management

Base path: `/api/users/{username}/tasks`

#### Get Tasks
```http
GET /api/users/{username}/tasks
```

**Query Parameters:**
- `date` (optional): Get tasks for specific date (YYYY-MM-DD)
- `startDate` & `endDate` (optional): Get tasks for date range
- `starred` (optional): Get only starred tasks (`true`)
- `incomplete` (optional): Get only incomplete tasks (`true`)

**Response:**
```json
[
  {
    "id": 1,
    "title": "Complete project documentation",
    "description": "Write comprehensive API docs",
    "date": "2024-01-01",
    "completed": false,
    "starred": true,
    "username": "john.doe",
    "createdAt": "2024-01-01T10:00:00",
    "updatedAt": "2024-01-01T10:00:00"
  }
]
```

#### Create Task
```http
POST /api/users/{username}/tasks
```

**Request Body:**
```json
{
  "title": "Complete project documentation",
  "description": "Write comprehensive API docs",
  "date": "2024-01-01",
  "username": "john.doe"
}
```

**Validation Rules:**
- Title: Required, max 200 characters
- Description: Optional, max 1000 characters
- Date: Required, cannot be more than 1 year in the future

#### Update Task
```http
PUT /api/users/{username}/tasks/{id}
```

#### Toggle Task Completion
```http
PATCH /api/users/{username}/tasks/{id}/complete
```

#### Toggle Task Star
```http
PATCH /api/users/{username}/tasks/{id}/star
```

#### Get Task Statistics
```http
GET /api/users/{username}/tasks/stats?date=2024-01-01
```

**Response:**
```json
{
  "totalTasks": 10,
  "completedTasks": 7,
  "starredTasks": 3,
  "incompleteTasks": 3
}
```

### 3. Reflection Management

Base path: `/api/users/{username}/reflections`

#### Get Reflections
```http
GET /api/users/{username}/reflections
```

**Query Parameters:**
- `date` (optional): Get reflection for specific date
- `startDate` & `endDate` (optional): Get reflections for date range
- `minRating` & `maxRating` (optional): Filter by energy rating range

**Response:**
```json
[
  {
    "id": 1,
    "date": "2024-01-01",
    "energyRating": 8,
    "reflectionText": "Great day, feeling productive!",
    "username": "john.doe",
    "createdAt": "2024-01-01T10:00:00",
    "updatedAt": "2024-01-01T10:00:00"
  }
]
```

#### Create/Update Reflection
```http
POST /api/users/{username}/reflections
```

**Request Body:**
```json
{
  "date": "2024-01-01",
  "energyRating": 8,
  "reflectionText": "Great day, feeling productive!",
  "username": "john.doe"
}
```

**Validation Rules:**
- Date: Required, cannot be older than 30 days
- Energy Rating: Required, 1-10 range
- Reflection Text: Optional, max 2000 characters

#### Get Today's Reflection
```http
GET /api/users/{username}/reflections/today
```

#### Get Average Rating
```http
GET /api/users/{username}/reflections/average-rating?startDate=2024-01-01&endDate=2024-01-07
```

### 4. Energy Assessment Management

Base path: `/api/users/{username}/energy`

#### Get Energy Assessments
```http
GET /api/users/{username}/energy
```

**Response:**
```json
[
  {
    "id": 1,
    "date": "2024-01-01",
    "energyLevel": 4,
    "username": "john.doe",
    "createdAt": "2024-01-01T10:00:00",
    "updatedAt": "2024-01-01T10:00:00"
  }
]
```

#### Create/Update Energy Assessment
```http
POST /api/users/{username}/energy
```

**Request Body:**
```json
{
  "date": "2024-01-01",
  "energyLevel": 4,
  "username": "john.doe"
}
```

**Validation Rules:**
- Date: Required, cannot be older than 30 days
- Energy Level: Required, 1-5 range

### 5. Combined Daily Data

#### Get Daily Data
```http
GET /api/users/{username}/daily-data?date=2024-01-01
```

**Response:**
```json
{
  "username": "john.doe",
  "date": "2024-01-01",
  "tasks": [
    {
      "id": 1,
      "title": "Complete project",
      "description": "Finish the daily task app",
      "date": "2024-01-01",
      "completed": true,
      "starred": false,
      "username": "john.doe",
      "createdAt": "2024-01-01T10:00:00",
      "updatedAt": "2024-01-01T10:00:00"
    }
  ],
  "reflection": {
    "id": 1,
    "date": "2024-01-01",
    "energyRating": 8,
    "reflectionText": "Productive day!",
    "username": "john.doe",
    "createdAt": "2024-01-01T20:00:00",
    "updatedAt": "2024-01-01T20:00:00"
  },
  "energyAssessment": {
    "id": 1,
    "date": "2024-01-01",
    "energyLevel": 4,
    "username": "john.doe",
    "createdAt": "2024-01-01T09:00:00",
    "updatedAt": "2024-01-01T09:00:00"
  },
  "stats": {
    "totalTasks": 1,
    "completedTasks": 1,
    "incompleteTasks": 0,
    "starredTasks": 0,
    "averageEnergyRating": 8.0,
    "averageEnergyLevel": 4.0
  }
}
```

#### Get Daily Data Range
```http
GET /api/users/{username}/daily-data/range?startDate=2024-01-01&endDate=2024-01-07
```

## 🚨 Error Handling

### Error Response Format
```json
{
  "timestamp": "2024-01-01T12:00:00",
  "status": 400,
  "error": "Validation Failed",
  "message": "Invalid input data provided",
  "path": "uri=/api/users/john.doe/tasks"
}
```

### Validation Error Response
```json
{
  "timestamp": "2024-01-01T12:00:00",
  "status": 400,
  "error": "Validation Failed",
  "message": "Invalid input data provided",
  "path": "uri=/api/users/john.doe/tasks",
  "validationErrors": {
    "title": "Title is required",
    "energyRating": "Energy rating must be between 1 and 10"
  }
}
```

### HTTP Status Codes
- **200**: Successful GET, PUT, PATCH requests
- **201**: Successful POST requests
- **204**: Successful DELETE requests
- **400**: Validation errors, business rule violations
- **404**: Resource not found
- **409**: Duplicate resource, data integrity violations
- **500**: Internal server error

## 🔄 Frontend Integration Examples

### User Registration Flow
```javascript
// Check if username is available
const checkUsername = async (username) => {
  const response = await fetch(`/api/users/${username}/exists`);
  return !(await response.json());
};

// Create new user
const createUser = async (username) => {
  const response = await fetch('/api/users', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ username })
  });
  return response.json();
};
```

### Daily Dashboard Data
```javascript
// Get complete daily data for dashboard
const getDailyData = async (username, date = null) => {
  const dateParam = date ? `?date=${date}` : '';
  const response = await fetch(`/api/users/${username}/daily-data${dateParam}`);
  return response.json();
};
```

### Task Management
```javascript
// Create new task
const createTask = async (username, taskData) => {
  const response = await fetch(`/api/users/${username}/tasks`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ ...taskData, username })
  });
  return response.json();
};

// Toggle task completion
const toggleTaskCompletion = async (username, taskId) => {
  const response = await fetch(`/api/users/${username}/tasks/${taskId}/complete`, {
    method: 'PATCH'
  });
  return response.json();
};
```

## 🧪 Testing with cURL

```bash
# Create user
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{"username": "john.doe"}'

# Create task
curl -X POST http://localhost:8080/api/users/john.doe/tasks \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Test task",
    "description": "This is a test",
    "date": "2024-01-01",
    "username": "john.doe"
  }'

# Get daily data
curl http://localhost:8080/api/users/john.doe/daily-data?date=2024-01-01
```

## 📝 Development Notes

### CORS Configuration
- `http://localhost:3000` (React)
- `http://localhost:5173` (Vite)

### Date Format
All dates: `YYYY-MM-DD` (ISO 8601)

### Business Rules
- Max 50 tasks per day per user
- Reflections: max 1 day in future
- Energy assessments: max 1 day in future
- Date ranges: max 365 days
- Data queries: max 2 years old

### Performance Tips
- Use date ranges wisely
- Utilize combined daily-data endpoint for dashboards
- Cache user existence checks
- Handle loading states properly 