# Database Schema Fix Documentation

## Issue
Backend failed to start with error:
```
org.h2.jdbc.JdbcSQLSyntaxErrorException: Syntax error in SQL statement "CREATE [*]DATABASE IF NOT EXISTS algoviz_code_execution"
```

## Root Cause
The `schema.sql` file contained MySQL-specific SQL commands that are incompatible with H2 in-memory database:
- `CREATE DATABASE IF NOT EXISTS` - Not supported by H2
- `USE database_name` - Not supported by H2  
- `ENUM` types - H2 has different syntax
- `ON UPDATE CURRENT_TIMESTAMP` - H2 has different syntax

## Solution Applied

### 1. **Disabled schema.sql Initialization**
- Renamed `src/main/resources/schema.sql` → `schema.sql.bak`
- Updated `application.properties`:
  ```properties
  spring.jpa.defer-datasource-initialization=false
  spring.sql.init.mode=never
  ```

### 2. **Why This Works**
Since the project uses:
- `spring.jpa.hibernate.ddl-auto=create-drop` - Hibernate auto-creates schema from JPA entities
- H2 in-memory database - Tables are created automatically on startup
- JPA annotations on entity classes - Define the schema structure

The `schema.sql` file is redundant and conflicts with Hibernate's auto-creation.

## Configuration Details

### application.properties (Active Configuration)
```properties
# Database Configuration - H2 (In-memory for development)
spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE
spring.datasource.driver-class-name=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=

# JPA Configuration
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.defer-datasource-initialization=false
spring.sql.init.mode=never  # ← Prevents schema.sql from being loaded
```

### application.yml (MySQL Configuration - Not Active)
This file is configured for MySQL but is overridden by `application.properties`. Spring Boot gives precedence to `.properties` files over `.yml` files.

## Result
✅ Backend starts successfully on port 8083
✅ H2 console available at: http://localhost:8083/api/h2-console
✅ All JPA entities auto-create their tables via Hibernate
✅ Database URL: `jdbc:h2:mem:testdb`

## Database Access
To view the in-memory database:
1. Navigate to: http://localhost:8083/api/h2-console
2. Enter connection details:
   - JDBC URL: `jdbc:h2:mem:testdb`
   - Username: `sa`
   - Password: (leave empty)

## Tables Created Automatically
Based on JPA entities, the following tables are auto-created:
- `users`
- `user_progress`
- `user_badges`
- `user_streaks`
- `user_activities`
- `problems`
- `test_cases`
- `starter_codes`
- `submissions`
- `quizzes`
- `quiz_questions`
- `quiz_submissions`
- `quiz_attempts`
- `exercises`

## Migration to Production MySQL (Future)
When deploying to production with MySQL:
1. Remove or comment out H2 configuration in `application.properties`
2. Activate MySQL configuration in `application.yml` or create `application-prod.properties`
3. Set `spring.jpa.hibernate.ddl-auto=validate` (never use `create-drop` in production)
4. Use Flyway or Liquibase for database migrations
5. Convert `schema.sql` to proper migration scripts

## Warning Fixed
Also fixed a Hibernate warning by updating to use H2Dialect:
```properties
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
```

## Files Modified
1. ✅ `src/main/resources/application.properties` - Disabled SQL initialization
2. ✅ `src/main/resources/schema.sql` → `schema.sql.bak` - Renamed to prevent loading

## Build Status
```
[INFO] BUILD SUCCESS
[INFO] Tomcat started on port 8083 (http) with context path '/api'
[INFO] Started BackendApplication in 14.899 seconds
```

---
*Fix applied on: October 15, 2025*
*Backend now running successfully on port 8083* ✅
