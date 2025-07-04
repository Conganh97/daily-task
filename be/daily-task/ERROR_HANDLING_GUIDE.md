# Error Handling Guide - Daily Task API

## Overview

This document describes the comprehensive error handling strategy implemented in the Daily Task API, including all exception types, HTTP status codes, and error response formats.

## Error Response Formats

### Standard Error Response
```json
{
  "timestamp": "2024-01-01T12:00:00",
  "status": 404,
  "error": "Resource Not Found",
  "message": "User not found with username: 'john.doe'",
  "path": "uri=/api/users/john.doe"
}
```

### Validation Error Response
```json
{
  "timestamp": "2024-01-01T12:00:00",
  "status": 400,
  "error": "Validation Failed",
  "message": "Invalid input data provided",
  "path": "uri=/api/tasks",
  "validationErrors": {
    "title": "Title is required",
    "energyRating": "Energy rating must be between 1 and 10"
  }
}
```

## HTTP Status Codes

| Status Code | Description | When Used |
|-------------|-------------|-----------|
| 400 | Bad Request | Invalid input, validation errors, business rule violations |
| 404 | Not Found | Resource not found (user, task, reflection, etc.) |
| 405 | Method Not Allowed | Unsupported HTTP method for endpoint |
| 409 | Conflict | Duplicate resource, data integrity violations |
| 500 | Internal Server Error | Unexpected server errors |

## Exception Types and Handling

### 1. ResourceNotFoundException (404)
**Triggered when**: Requested entity doesn't exist
**Examples**:
- User not found by username
- Task not found by ID
- Reflection not found for date
- Energy assessment not found

```json
{
  "status": 404,
  "error": "Resource Not Found",
  "message": "Task not found with id: '123'"
}
```

### 2. DuplicateResourceException (409)
**Triggered when**: Attempting to create duplicate resource
**Examples**:
- Username already exists
- Duplicate reflection for same date

```json
{
  "status": 409,
  "error": "Duplicate Resource",
  "message": "User already exists with username: 'john.doe'"
}
```

### 3. BusinessRuleViolationException (400)
**Triggered when**: Business logic rules are violated
**Examples**:
- Too many tasks per day (>50)
- Creating reflection too far in future
- Deleting user with recent activity
- Date range exceeds limits

```json
{
  "status": 400,
  "error": "Business Rule Violation",
  "message": "Cannot create more than 50 tasks per day. Current count: 50"
}
```

### 4. DataIntegrityViolationException (409)
**Triggered when**: Database constraints are violated
**Examples**:
- Foreign key violations
- Unique constraint violations
- Database-level integrity issues

```json
{
  "status": 409,
  "error": "Data Integrity Violation",
  "message": "Username already exists"
}
```

### 5. Validation Errors (400)

#### Bean Validation (MethodArgumentNotValidException)
**Triggered when**: Request DTO validation fails
**Examples**:
- Required fields missing
- Field length violations
- Format validation failures

```json
{
  "status": 400,
  "error": "Validation Failed",
  "validationErrors": {
    "username": "Username is required",
    "energyRating": "Energy rating must be at least 1"
  }
}
```

#### Constraint Validation (ConstraintViolationException)
**Triggered when**: Method parameter validation fails
**Examples**:
- Path variable validation
- Request parameter validation

```json
{
  "status": 400,
  "error": "Constraint Violation",
  "validationErrors": {
    "getUserByUsername.username": "Username must be valid"
  }
}
```

### 6. Request Processing Errors (400)

#### Invalid JSON (HttpMessageNotReadableException)
**Triggered when**: Malformed JSON in request body

```json
{
  "status": 400,
  "error": "Invalid Request Body",
  "message": "Invalid JSON format in request body"
}
```

#### Type Mismatch (MethodArgumentTypeMismatchException)
**Triggered when**: Parameter type conversion fails

```json
{
  "status": 400,
  "error": "Type Mismatch",
  "message": "Invalid value 'abc' for parameter 'id'. Expected type: Long"
}
```

#### Missing Parameter (MissingServletRequestParameterException)
**Triggered when**: Required parameter is missing

```json
{
  "status": 400,
  "error": "Missing Parameter",
  "message": "Required parameter 'date' of type 'LocalDate' is missing"
}
```

### 7. Method Not Allowed (405)
**Triggered when**: HTTP method not supported for endpoint

```json
{
  "status": 405,
  "error": "Method Not Allowed",
  "message": "HTTP method 'POST' is not supported for this endpoint. Supported methods: GET, PUT"
}
```

### 8. IllegalArgumentException (400)
**Triggered when**: Invalid argument provided to method
**Examples**:
- Invalid enum values
- Null arguments where not allowed

```json
{
  "status": 400,
  "error": "Invalid Argument",
  "message": "Start date cannot be after end date"
}
```

### 9. Generic Exception (500)
**Triggered when**: Unexpected server error occurs

```json
{
  "status": 500,
  "error": "Internal Server Error",
  "message": "An unexpected error occurred. Please try again later."
}
```

## Custom Validation Rules

### Username Validation (@ValidUsername)
- 3-50 characters long
- Only letters, numbers, dots, hyphens, underscores
- Cannot start/end with special characters
- No consecutive special characters
- Reserved usernames are blocked

### Date Validation (@ValidDate)
- Configurable past/future restrictions
- Maximum days in past/future limits
- Business-specific date rules

### Business Rules
- Maximum 50 tasks per day per user
- Reflections cannot be created >1 day in future
- Energy assessments cannot be created >1 day in future
- Date ranges limited to 365 days
- Cannot query data older than 2 years
- Users with recent activity cannot be deleted

## Error Handling Best Practices

### For Clients
1. **Always check HTTP status codes** before processing response
2. **Handle validation errors** by displaying field-specific messages
3. **Implement retry logic** for 500 errors with exponential backoff
4. **Display user-friendly messages** for business rule violations
5. **Log detailed error information** for debugging

### For Developers
1. **Use specific exception types** instead of generic exceptions
2. **Provide clear, actionable error messages**
3. **Include relevant context** in error messages
4. **Validate input early** at the DTO level
5. **Apply business rules consistently** across all operations

## Testing Error Scenarios

### Common Test Cases
1. **Validation Tests**
   - Empty required fields
   - Invalid field formats
   - Boundary value testing

2. **Business Rule Tests**
   - Exceeding limits (tasks per day)
   - Invalid date combinations
   - Constraint violations

3. **Resource Tests**
   - Non-existent resources
   - Duplicate creation attempts
   - Permission violations

4. **Edge Cases**
   - Malformed JSON
   - Invalid HTTP methods
   - Concurrent access scenarios

## Monitoring and Logging

### Error Metrics to Track
- Error rate by endpoint
- Most common validation errors
- Business rule violation frequency
- Response time for error scenarios

### Logging Strategy
- **INFO**: Successful operations
- **WARN**: Business rule violations, validation failures
- **ERROR**: Unexpected exceptions, system errors
- **DEBUG**: Detailed request/response data (development only)

This comprehensive error handling approach ensures consistent, informative, and user-friendly error responses across the entire API. 