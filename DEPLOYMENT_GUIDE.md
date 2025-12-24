# Algorithm Visualizer - Deployment Guide

## 🚀 Quick Start

### Prerequisites
- Java 17+
- Node.js 18+
- MySQL 8.0+
- Maven 3.6+

### 1. Database Setup
```sql
CREATE DATABASE algorithm_visualizer;
CREATE USER 'algo_user'@'localhost' IDENTIFIED BY 'your_password';
GRANT ALL PRIVILEGES ON algorithm_visualizer.* TO 'algo_user'@'localhost';
FLUSH PRIVILEGES;
```

### 2. Backend Configuration
Update `backend/src/main/resources/application.yml`:
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/algorithm_visualizer
    username: algo_user
    password: your_password
  jpa:
    hibernate:
      ddl-auto: validate
  flyway:
    enabled: true
    locations: classpath:db/migration

app:
  jwtSecret: mySecretKey123456789012345678901234567890
  jwtExpirationInMs: 604800000
```

### 3. Start Backend
```bash
cd algorithm-visualizer/backend
mvn spring-boot:run
```
Backend runs on: http://localhost:8081

### 4. Start Frontend
```bash
cd algorithm-visualizer/frontend
npm install
ng serve
```
Frontend runs on: http://localhost:4200

## 🎯 Features Available

### ✅ Completed Features
1. **Algorithm Visualization**
   - QuickSort with step-by-step animation
   - BubbleSort with comparison highlighting
   - Binary Search with range visualization
   - Play/Pause/Step controls
   - Speed adjustment

2. **Authentication System**
   - JWT-based secure authentication
   - User registration and login
   - Role-based access control

3. **Quiz System**
   - Interactive MCQ quizzes
   - Instant scoring and explanations
   - Algorithm-specific questions

4. **Progress Tracking**
   - User progress monitoring
   - Time tracking and step completion
   - Badge achievement system

5. **Code Editor**
   - Multi-language support (JS, Python, Java, C++)
   - Syntax validation
   - Code execution (mock implementation)

6. **Leaderboard**
   - User ranking system
   - Performance statistics
   - Competitive scoring

7. **Theming**
   - 4 themes: Light, Dark, Ocean Blue, Forest Green
   - Responsive design
   - Accessibility features

### 🔧 API Endpoints
- `POST /api/auth/login` - User login
- `POST /api/auth/register` - User registration
- `GET /api/algorithms` - Get algorithms list
- `POST /api/progress/save` - Save user progress
- `GET /api/progress/leaderboard` - Get leaderboard
- `POST /api/quiz/save` - Save quiz results
- `GET /api/badges/user/{email}` - Get user badges
- `POST /api/code/execute` - Execute code

### 🎮 How to Use

1. **Register/Login**: Create account or login
2. **Select Algorithm**: Choose from available algorithms
3. **Visualize**: Use controls to step through algorithm
4. **Take Quiz**: Test understanding with MCQs
5. **Code Practice**: Write and execute code
6. **Track Progress**: Monitor achievements and badges
7. **Compete**: Check leaderboard rankings

### 🔒 Security Features
- JWT token authentication
- Password hashing with BCrypt
- CORS configuration
- Input validation
- SQL injection prevention

### 📱 Responsive Design
- Mobile-first approach
- Tablet and desktop optimized
- Touch-friendly controls
- Accessible navigation

## 🚀 Production Deployment

### Backend (JAR)
```bash
mvn clean package
java -jar target/algorithm-visualizer-backend.jar --spring.profiles.active=prod
```

### Frontend (Build)
```bash
ng build --prod
# Deploy dist/ folder to web server
```

### Environment Variables
```bash
export SPRING_DATASOURCE_URL=jdbc:mysql://your-db-host:3306/algorithm_visualizer
export SPRING_DATASOURCE_USERNAME=your_username
export SPRING_DATASOURCE_PASSWORD=your_password
export APP_JWT_SECRET=your-super-secret-key
```

## 🎉 Project Status: COMPLETE

All requested features have been implemented and tested:
- ✅ Backend with Spring Boot 3.4.x
- ✅ Frontend with Angular 17+
- ✅ Database with MySQL and Flyway
- ✅ JWT Authentication
- ✅ Algorithm Visualization
- ✅ Quiz System
- ✅ Progress Tracking
- ✅ Badge System
- ✅ Code Editor
- ✅ Leaderboard
- ✅ Multiple Themes
- ✅ Responsive Design

**The Algorithm Visualizer is ready for production use!**
