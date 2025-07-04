# Daily Task API Documentation for Frontend Integration

## 📋 Overview

This document provides comprehensive API documentation for the Daily Task application backend, designed specifically for frontend integration. The API follows REST principles and uses JSON for data exchange.

**Base URL**: `http://localhost:8080/api`  
**Authentication**: Username-based (no tokens required)  
**Content-Type**: `application/json`

## 🔐 Authentication

The API uses a simple username-based authentication system. No JWT tokens or API keys are required. Simply include the username in the URL path or request body as specified.

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

**Response:** `201 Created`
```json
{
  "id": 1,
  "username": "john.doe",
  "createdAt": "2024-01-01T10:00:00",
  "updatedAt": "2024-01-01T10:00:00"
}
```

#### Check Username Exists
```http
GET /api/users/{username}/exists
```

**Response:**
```json
true
```

#### Get User by Username
```http
GET /api/users/{username}
```

**Response:**
```json
{
  "id": 1,
  "username": "john.doe",
  "createdAt": "2024-01-01T10:00:00",
  "updatedAt": "2024-01-01T10:00:00"
}
```

#### Search Users
```http
GET /api/users/search?searchTerm=john
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

#### Delete User
```http
DELETE /api/users/{id}
```

**Response:** `204 No Content`

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

**Examples:**
```http
GET /api/users/john.doe/tasks?date=2024-01-01
GET /api/users/john.doe/tasks?startDate=2024-01-01&endDate=2024-01-07
GET /api/users/john.doe/tasks?starred=true
GET /api/users/john.doe/tasks?incomplete=true
```

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
- Username: Required, must be valid

**Response:** `201 Created`
```json
{
  "id": 1,
  "title": "Complete project documentation",
  "description": "Write comprehensive API docs",
  "date": "2024-01-01",
  "completed": false,
  "starred": false,
  "username": "john.doe",
  "createdAt": "2024-01-01T10:00:00",
  "updatedAt": "2024-01-01T10:00:00"
}
```

#### Update Task
```http
PUT /api/users/{username}/tasks/{id}
```

**Request Body:**
```json
{
  "title": "Updated task title",
  "description": "Updated description",
  "completed": true,
  "starred": true
}
```

#### Toggle Task Completion
```http
PATCH /api/users/{username}/tasks/{id}/complete
```

**Response:**
```json
{
  "id": 1,
  "completed": true,
  // ... other fields
}
```

#### Toggle Task Star
```http
PATCH /api/users/{username}/tasks/{id}/star
```

**Response:**
```json
{
  "id": 1,
  "starred": true,
  // ... other fields
}
```

#### Get Task by ID
```http
GET /api/users/{username}/tasks/{id}
```

#### Delete Task
```http
DELETE /api/users/{username}/tasks/{id}
```

**Response:** `204 No Content`

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

**Examples:**
```http
GET /api/users/john.doe/reflections?date=2024-01-01
GET /api/users/john.doe/reflections?startDate=2024-01-01&endDate=2024-01-07
GET /api/users/john.doe/reflections?minRating=7&maxRating=10
```

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
- Username: Required, must be valid

#### Get Today's Reflection
```http
GET /api/users/{username}/reflections/today
```

#### Check Reflection Exists
```http
GET /api/users/{username}/reflections/exists?date=2024-01-01
```

**Response:**
```json
true
```

#### Get Average Rating
```http
GET /api/users/{username}/reflections/average-rating
GET /api/users/{username}/reflections/average-rating?startDate=2024-01-01&endDate=2024-01-07
```

**Response:**
```json
{
  "averageRating": 7.5,
  "startDate": "2024-01-01",
  "endDate": "2024-01-07"
}
```

### 4. Energy Assessment Management

Base path: `/api/users/{username}/energy`

#### Get Energy Assessments
```http
GET /api/users/{username}/energy
```

**Query Parameters:**
- `date` (optional): Get assessment for specific date
- `startDate` & `endDate` (optional): Get assessments for date range
- `minLevel` & `maxLevel` (optional): Filter by energy level range

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
- Username: Required, must be valid

#### Get Today's Energy Assessment
```http
GET /api/users/{username}/energy/today
```

#### Get Average Energy Level
```http
GET /api/users/{username}/energy/average-level
```

**Response:**
```json
{
  "averageLevel": 3.7,
  "startDate": null,
  "endDate": null
}
```

### 5. Combined Daily Data

Base path: `/api/users/{username}/daily-data`

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

**Response:** Array of DailyDataResponse objects

## 🚨 Error Handling

The API returns structured error responses with appropriate HTTP status codes:

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

| Status Code | Description | When Used |
|-------------|-------------|-----------|
| 200 | OK | Successful GET, PUT, PATCH requests |
| 201 | Created | Successful POST requests |
| 204 | No Content | Successful DELETE requests |
| 400 | Bad Request | Validation errors, business rule violations |
| 404 | Not Found | Resource not found |
| 405 | Method Not Allowed | Unsupported HTTP method |
| 409 | Conflict | Duplicate resource, data integrity violations |
| 500 | Internal Server Error | Unexpected server errors |

### Common Error Scenarios

#### Username Validation Error
```json
{
  "status": 400,
  "error": "Validation Failed",
  "validationErrors": {
    "username": "Username cannot start or end with a dot"
  }
}
```

#### Business Rule Violation
```json
{
  "status": 400,
  "error": "Business Rule Violation",
  "message": "Cannot create more than 50 tasks per day. Current count: 50"
}
```

#### Resource Not Found
```json
{
  "status": 404,
  "error": "Resource Not Found",
  "message": "User not found with username: 'nonexistent'"
}
```

## 🔄 Common Frontend Use Cases

### 1. User Registration Flow
```javascript
// Check if username is available
const checkUsername = async (username) => {
  const response = await fetch(`/api/users/${username}/exists`);
  const exists = await response.json();
  return !exists;
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

### 2. Daily Dashboard Data
```javascript
// Get complete daily data for dashboard
const getDailyData = async (username, date = null) => {
  const dateParam = date ? `?date=${date}` : '';
  const response = await fetch(`/api/users/${username}/daily-data${dateParam}`);
  return response.json();
};
```

### 3. Task Management
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

### 4. Analytics Data
```javascript
// Get task statistics
const getTaskStats = async (username, date) => {
  const response = await fetch(`/api/users/${username}/tasks/stats?date=${date}`);
  return response.json();
};

// Get reflection average rating
const getAverageRating = async (username, startDate, endDate) => {
  const response = await fetch(
    `/api/users/${username}/reflections/average-rating?startDate=${startDate}&endDate=${endDate}`
  );
  return response.json();
};
```

## 🧪 Testing the API

### Using cURL

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

### Using JavaScript Fetch

```javascript
// Base configuration
const API_BASE = 'http://localhost:8080/api';

const apiRequest = async (endpoint, options = {}) => {
  const response = await fetch(`${API_BASE}${endpoint}`, {
    headers: {
      'Content-Type': 'application/json',
      ...options.headers
    },
    ...options
  });
  
  if (!response.ok) {
    const error = await response.json();
    throw new Error(`API Error: ${error.message}`);
  }
  
  return response.json();
};

// Example usage
try {
  const dailyData = await apiRequest('/users/john.doe/daily-data?date=2024-01-01');
  console.log(dailyData);
} catch (error) {
  console.error('Failed to fetch daily data:', error.message);
}
```

## 📝 Development Notes

### CORS Configuration
The API is configured to accept requests from:
- `http://localhost:3000` (React development server)
- `http://localhost:5173` (Vite development server)

### Date Format
All dates should be in ISO 8601 format: `YYYY-MM-DD`

### Business Rules
- Maximum 50 tasks per day per user
- Reflections cannot be created more than 1 day in the future
- Energy assessments cannot be created more than 1 day in the future
- Date ranges are limited to 365 days for performance
- Cannot query data older than 2 years

### Performance Tips
- Use date ranges wisely to avoid large data sets
- Utilize the combined daily-data endpoint for dashboard views
- Cache user existence checks for better UX
- Handle loading states for async operations

This documentation provides everything needed for frontend integration with the Daily Task API. For additional questions or clarifications, refer to the error handling guide and business validation rules. 