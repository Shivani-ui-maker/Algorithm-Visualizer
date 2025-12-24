# Algorithm Visualizer

A comprehensive web application for learning and visualizing algorithms with interactive features, progress tracking, and multi-language code execution.

## 🚀 Features

### Core Features
- **Interactive Algorithm Visualization**: Step-by-step visualization of sorting, searching, and graph algorithms
- **Multi-Language Code Editor**: Support for JavaScript, Python, Java, and C++ with syntax validation
- **Quiz System**: Interactive MCQ quizzes with instant scoring and explanations
- **Progress Tracking**: Track completion, time spent, and performance across algorithms
- **Badge System**: Earn achievements for completing algorithms and scoring well on quizzes
- **Leaderboard**: Compete with other users and track your ranking
- **User Authentication**: Secure JWT-based authentication with role management

### Algorithm Support
- **Sorting Algorithms**: Quick Sort, Bubble Sort, Merge Sort, Selection Sort
- **Search Algorithms**: Binary Search, Linear Search
- **Graph Algorithms**: BFS, DFS, Dijkstra's Algorithm
- **Data Structures**: Arrays, Trees, Graphs visualization

### UI/UX Features
- **Multiple Themes**: Light, Dark, Ocean Blue, and Forest Green themes
- **Responsive Design**: Works seamlessly on desktop, tablet, and mobile
- **Accessibility**: Full keyboard navigation and screen reader support
- **Real-time Visualization**: Smooth animations with play/pause/step controls

## 🛠 Technology Stack

### Backend
- **Framework**: Spring Boot 3.4.x
- **Database**: MySQL with Flyway migrations
- **Security**: JWT authentication with Spring Security
- **API**: RESTful APIs with comprehensive error handling

### Frontend
- **Framework**: Angular 17+ with standalone components
- **Visualization**: D3.js for interactive algorithm animations
- **Styling**: CSS custom properties with theme support
- **State Management**: RxJS for reactive programming

## 📦 Installation & Setup

### Prerequisites
- Java 17+
- Node.js 18+
- MySQL 8.0+
- Maven 3.6+

### Backend Setup
1. Clone the repository
```bash
git clone <repository-url>
cd algorithm-visualizer/backend
```

2. Configure database in `application.yml`
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/algorithm_visualizer
    username: your_username
    password: your_password
```

3. Run the application
```bash
mvn spring-boot:run
```

The backend will start on `http://localhost:8081`

### Frontend Setup
1. Navigate to frontend directory
```bash
cd algorithm-visualizer/frontend
```

2. Install dependencies
```bash
npm install
```

3. Start development server
```bash
ng serve
```

The frontend will start on `http://localhost:4200`

## 🎯 Usage

### Getting Started
1. Register a new account or login with existing credentials
2. Browse available algorithms from the dashboard
3. Select an algorithm to start visualization
4. Use play/pause/step controls to navigate through algorithm steps
5. Take quizzes to test your understanding
6. Track your progress and earn badges

### Algorithm Visualization
- **Play**: Auto-play the algorithm with adjustable speed
- **Pause**: Pause the current animation
- **Step Forward/Backward**: Navigate step-by-step through the algorithm
- **Reset**: Return to the initial state
- **Generate New Data**: Create new random data for visualization

### Code Editor
- Select your preferred programming language
- Write algorithm implementations
- Validate syntax before execution
- Run code with custom input
- View execution results and performance metrics

### Progress Tracking
- View completion status for each algorithm
- Track time spent and steps completed
- Monitor quiz scores and performance
- Earn badges for achievements
- Compare your progress on the leaderboard

## 🏗 Architecture

### Database Schema
- **Users**: User authentication and profile information
- **Algorithms**: Algorithm metadata and categories
- **UserProgress**: Individual progress tracking
- **Quizzes**: Quiz questions and answers
- **Badges**: Achievement system
- **UserBadges**: User badge assignments

### API Endpoints
- `POST /api/auth/login` - User authentication
- `POST /api/auth/register` - User registration
- `GET /api/algorithms` - Get all algorithms
- `POST /api/progress/save` - Save user progress
- `GET /api/progress/leaderboard` - Get leaderboard data
- `POST /api/quiz/save` - Save quiz results
- `POST /api/code/execute` - Execute code
- `GET /api/badges/user/{email}` - Get user badges

### Security
- JWT token-based authentication
- Role-based access control
- CORS configuration for cross-origin requests
- Input validation and sanitization

## 🎨 Theming

The application supports multiple themes:
- **Light Theme**: Clean, bright interface
- **Dark Theme**: Easy on the eyes for extended use
- **Ocean Blue**: Blue-tinted professional look
- **Forest Green**: Nature-inspired green theme

Themes can be switched from the theme selector in the navigation bar.

## 🧪 Testing

### Backend Testing
```bash
cd backend
mvn test
```

### Frontend Testing
```bash
cd frontend
ng test
```

## 🚀 Deployment

### Backend Deployment
1. Build the JAR file
```bash
mvn clean package
```

2. Run with production profile
```bash
java -jar target/algorithm-visualizer-backend.jar --spring.profiles.active=prod
```

### Frontend Deployment
1. Build for production
```bash
ng build --prod
```

2. Deploy the `dist/` folder to your web server

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 📝 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🙏 Acknowledgments

- D3.js for powerful data visualization capabilities
- Spring Boot for robust backend framework
- Angular for modern frontend development
- The open-source community for inspiration and tools

## 📞 Support

For support, email support@algorithmvisualizer.com or create an issue in the repository.

---

**Happy Learning! 🎓**
