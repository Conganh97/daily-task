# Database Schema Documentation

## Entity-Relationship Diagram

```
┌─────────────────┐
│      USERS      │
├─────────────────┤
│ id (PK)         │
│ username (UQ)   │
│ created_at      │
│ updated_at      │
└─────────────────┘
         │
         │ 1:N
         ▼
┌─────────────────┐
│      TASKS      │
├─────────────────┤
│ id (PK)         │
│ user_id (FK)    │
│ title           │
│ description     │
│ completed       │
│ starred         │
│ date            │
│ created_at      │
│ updated_at      │
└─────────────────┘

         │
         │ 1:N
         ▼
┌─────────────────┐
│   REFLECTIONS   │
├─────────────────┤
│ id (PK)         │
│ user_id (FK)    │
│ date (UQ)       │
│ energy_rating   │
│ reflection_text │
│ created_at      │
│ updated_at      │
└─────────────────┘

         │
         │ 1:N
         ▼
┌─────────────────┐
│ENERGY_ASSESSMENTS│
├─────────────────┤
│ id (PK)         │
│ user_id (FK)    │
│ date (UQ)       │
│ energy_level    │
│ created_at      │
│ updated_at      │
└─────────────────┘
```

## Tables

### Users
- **Primary Key**: `id` (BIGSERIAL)
- **Unique Constraint**: `username`
- **Relationships**: 
  - One-to-Many with Tasks
  - One-to-Many with Reflections
  - One-to-Many with Energy Assessments

### Tasks
- **Primary Key**: `id` (BIGSERIAL)
- **Foreign Key**: `user_id` → `users.id`
- **Indexes**: 
  - `(user_id, date)` - Main query pattern
  - `(user_id, starred)` - For starred tasks
  - `(user_id, completed)` - For completed tasks
  - `(date)` - For date-based queries

### Reflections
- **Primary Key**: `id` (BIGSERIAL)
- **Foreign Key**: `user_id` → `users.id`
- **Unique Constraint**: `(user_id, date)` - One reflection per user per day
- **Validation**: `energy_rating` must be between 1 and 10
- **Indexes**: 
  - `(user_id, date)` - Main query pattern
  - `(date)` - For date-based queries

### Energy Assessments
- **Primary Key**: `id` (BIGSERIAL)
- **Foreign Key**: `user_id` → `users.id`
- **Unique Constraint**: `(user_id, date)` - One assessment per user per day
- **Validation**: `energy_level` must be between 1 and 5
- **Indexes**: 
  - `(user_id, date)` - Main query pattern
  - `(date)` - For date-based queries

## Constraints

### Data Integrity
- All foreign keys have `ON DELETE CASCADE` to maintain referential integrity
- Username must be unique across all users
- Energy ratings are validated at database level
- Date-based unique constraints prevent duplicate entries

### Business Rules
- Each user can have multiple tasks per day
- Each user can have only one reflection per day
- Each user can have only one energy assessment per day
- Tasks can be completed and starred independently
- Reflections use 1-10 energy rating scale
- Energy assessments use 1-5 energy level scale

## Migration Files

1. **V1__Create_users_table.sql** - Creates users table with username constraint
2. **V2__Create_tasks_table.sql** - Creates tasks table with user relationship
3. **V3__Create_reflections_table.sql** - Creates reflections table with unique constraint
4. **V4__Create_energy_assessments_table.sql** - Creates energy assessments table
5. **V5__Insert_sample_data.sql** - Inserts sample data for testing

## Sample Data

The schema includes sample data for testing:
- 3 test users: `testuser1`, `testuser2`, `admin`
- 5 sample tasks with various states
- 3 sample reflections with different energy ratings
- 4 sample energy assessments with different levels

## Query Patterns

### Common Queries
- Get all tasks for a user on a specific date
- Get starred tasks for a user
- Get completed tasks for a user
- Get reflection for a user on a specific date
- Get energy assessment for a user on a specific date
- Get all data for a user on a specific date (combined query)

### Performance Considerations
- Indexes are optimized for user-date queries
- Lazy loading is used for entity relationships
- Foreign key constraints ensure data integrity
- Unique constraints prevent duplicate entries 