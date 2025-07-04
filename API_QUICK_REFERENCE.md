# Daily Task API - Quick Reference

**Base URL**: `http://localhost:8080/api`

## 👤 Users
```http
GET    /api/users                           # List all users
POST   /api/users                           # Create user
GET    /api/users/{username}/exists         # Check username exists
GET    /api/users/{username}                # Get user by username
```

## ✅ Tasks
```http
GET    /api/users/{username}/tasks          # Get tasks (with filters)
POST   /api/users/{username}/tasks          # Create task
PUT    /api/users/{username}/tasks/{id}     # Update task
DELETE /api/users/{username}/tasks/{id}     # Delete task
PATCH  /api/users/{username}/tasks/{id}/complete  # Toggle completion
PATCH  /api/users/{username}/tasks/{id}/star      # Toggle star
GET    /api/users/{username}/tasks/stats    # Get statistics
```

### Task Filters
- `?date=2024-01-01` - Tasks for specific date
- `?startDate=2024-01-01&endDate=2024-01-07` - Date range
- `?starred=true` - Only starred tasks
- `?incomplete=true` - Only incomplete tasks

## 🪞 Reflections
```http
GET    /api/users/{username}/reflections          # Get reflections (with filters)
POST   /api/users/{username}/reflections          # Create/update reflection
GET    /api/users/{username}/reflections/today    # Today's reflection
GET    /api/users/{username}/reflections/average-rating  # Average rating
```

## ⚡ Energy Assessments
```http
GET    /api/users/{username}/energy              # Get energy assessments
POST   /api/users/{username}/energy              # Create/update assessment
GET    /api/users/{username}/energy/today        # Today's assessment
GET    /api/users/{username}/energy/average-level # Average level
```

## 📊 Combined Data
```http
GET    /api/users/{username}/daily-data          # Complete daily data
GET    /api/users/{username}/daily-data/range    # Date range data
```

## 📝 Request Examples

### Create User
```json
POST /api/users
{
  "username": "john.doe"
}
```

### Create Task
```json
POST /api/users/john.doe/tasks
{
  "title": "Complete project",
  "description": "Finish the API docs",
  "date": "2024-01-01",
  "username": "john.doe"
}
```

### Create Reflection
```json
POST /api/users/john.doe/reflections
{
  "date": "2024-01-01",
  "energyRating": 8,
  "reflectionText": "Great productive day!",
  "username": "john.doe"
}
```

### Create Energy Assessment
```json
POST /api/users/john.doe/energy
{
  "date": "2024-01-01",
  "energyLevel": 4,
  "username": "john.doe"
}
```

## 🚨 Common Errors

| Status | Error | Description |
|--------|-------|-------------|
| 400 | Validation Failed | Invalid input data |
| 404 | Resource Not Found | User/task/reflection not found |
| 409 | Duplicate Resource | Username exists, duplicate reflection |
| 500 | Internal Server Error | Server error |

## 📱 Frontend Helpers

### Check Username
```javascript
const isUsernameAvailable = async (username) => {
  const response = await fetch(`/api/users/${username}/exists`);
  return !(await response.json());
};
```

### Get Daily Data
```javascript
const getDailyData = async (username, date) => {
  const response = await fetch(`/api/users/${username}/daily-data?date=${date}`);
  return response.json();
};
```

### Toggle Task
```javascript
const toggleTask = async (username, taskId) => {
  const response = await fetch(`/api/users/${username}/tasks/${taskId}/complete`, {
    method: 'PATCH'
  });
  return response.json();
};
``` 