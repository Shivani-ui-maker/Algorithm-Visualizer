# 📚 Algorithm Visualizer - Complete Project Documentation

## 🎯 Project Overview

**Algorithm Visualizer** is a full-stack educational web application designed to help students learn Data Structures and Algorithms (DSA) and Design and Analysis of Algorithms (DAA) through interactive visualizations, quizzes, code execution, and gamification.

---

## 🏗️ Technology Stack

### **Backend**
- **Framework**: Spring Boot 3.4.0
- **Language**: Java 21
- **Build Tool**: Maven 3.11.0
- **Database**: H2 (In-memory for development)
- **Security**: Spring Security with JWT
- **Database Migration**: Flyway
- **Object Mapping**: MapStruct 1.5.5
- **JWT Library**: jjwt 0.11.5
- **Utility**: Lombok 1.18.40

### **Frontend**
- **Framework**: Angular 20.0.0
- **Language**: TypeScript 5.8.2
- **UI Framework**: Bootstrap 5.3.7
- **Icons**: Bootstrap Icons 1.13.1
- **Visualization**: D3.js 7.9.0, GSAP 3.13.0
- **Code Editor**: Monaco Editor 0.51.0
- **Charts**: Chart.js 4.4.4
- **HTTP Client**: RxJS 7.8.0

### **Development Tools**
- **Testing**: JUnit (Backend), Jasmine/Karma (Frontend)
- **Package Manager**: npm
- **Version Control**: Git

---

## 📁 Project Architecture

### **Backend Structure**
```
backend/
├── src/main/java/com/algo/backend/
│   ├── controller/          # REST API endpoints
│   │   ├── AuthController.java
│   │   ├── ProgressController.java
│   │   ├── QuizController.java
│   │   ├── CodeExecutionController.java
│   │   ├── GamificationController.java
│   │   └── LeaderboardController.java
│   ├── entity/              # JPA entities
│   │   ├── User.java
│   │   ├── Algorithm.java
│   │   ├── UserProgress.java
│   │   ├── Quiz.java
│   │   ├── QuizAttempt.java
│   │   ├── Badge.java
│   │   └── UserBadge.java
│   ├── repository/          # Data access layer
│   │   ├── UserRepository.java
│   │   ├── AlgorithmRepository.java
│   │   ├── ProgressRepository.java
│   │   ├── QuizRepository.java
│   │   └── BadgeRepository.java
│   ├── service/             # Business logic
│   │   ├── AuthService.java
│   │   ├── AlgorithmService.java
│   │   ├── ProgressService.java
│   │   ├── QuizService.java
│   │   ├── BadgeService.java
│   │   └── CodeExecutionService.java
│   ├── security/            # Security configuration
│   │   ├── JwtAuthenticationFilter.java
│   │   ├── JwtUtil.java
│   │   ├── UserDetailsImpl.java
│   │   └── SecurityConfig.java
│   ├── dto/                 # Data transfer objects
│   │   ├── LoginRequest.java
│   │   ├── SignupRequest.java
│   │   ├── JwtResponse.java
│   │   └── MessageResponse.java
│   └── util/                # Utility classes
│       └── JwtUtil.java
└── src/main/resources/
    ├── application.yml      # Configuration
    ├── schema.sql           # Database schema
    └── db/migration/        # Flyway migrations
```

### **Frontend Structure**
```
frontend/src/
├── app/
│   ├── components/          # Reusable components
│   │   ├── advanced-visualizer/
│   │   ├── code-editor/
│   │   ├── leaderboard/
│   │   ├── theme-selector/
│   │   └── user-dashboard/
│   ├── pages/               # Page components
│   │   ├── home/
│   │   ├── login/
│   │   ├── dsa/             # DSA visualizations
│   │   ├── daa/             # DAA visualizations
│   │   ├── exercises/
│   │   ├── quiz/
│   │   ├── progress/
│   │   ├── leaderboard/
│   │   ├── settings/
│   │   ├── faqs/
│   │   └── code-editor/
│   ├── services/            # Angular services
│   │   ├── auth.service.ts
│   │   ├── algorithm.service.ts
│   │   ├── progress.service.ts
│   │   ├── quiz.service.ts
│   │   ├── audio.service.ts
│   │   └── gamification.service.ts
│   ├── guards/              # Route guards
│   │   └── auth.guard.ts
│   ├── models/              # TypeScript interfaces
│   │   └── algo-step.model.ts
│   ├── shared/              # Shared components
│   │   ├── navbar/
│   │   └── algorithm-skeleton.component.ts
│   ├── visualizations/      # Algorithm visualizations
│   │   ├── sorting/
│   │   ├── searching/
│   │   ├── trees/
│   │   └── graphs/
│   ├── app.routes.ts        # Routing configuration
│   └── app.config.ts        # App configuration
└── assets/                  # Static assets
    ├── sounds/              # Sound effects
    └── images/
```

---

## 🔐 Authentication System

### **Implementation Details**

#### **Technology**
- **JWT (JSON Web Token)** for stateless authentication
- **Spring Security** for backend security
- **Local Storage** for token persistence
- **HTTP Interceptors** for automatic token attachment

#### **Flow Diagram**
```
User → Login Page → AuthService → Backend API → JWT Token
                                        ↓
                          Store in localStorage
                                        ↓
                        Set in currentUserSubject
                                        ↓
                    Attach to all HTTP requests
```

#### **Backend Implementation**

**1. JWT Utility (`JwtUtil.java`)**
```java
@Component
public class JwtUtil {
    @Value("${app.jwtSecret}")
    private String jwtSecret;
    
    @Value("${app.jwtExpirationInMs}")
    private int jwtExpirationInMs; // 7 days
    
    // Generate token from email
    public String generateToken(String email) {
        Date expiryDate = new Date(System.currentTimeMillis() + jwtExpirationInMs);
        return Jwts.builder()
            .setSubject(email)
            .setIssuedAt(new Date())
            .setExpiration(expiryDate)
            .signWith(getSigningKey(), SignatureAlgorithm.HS512)
            .compact();
    }
    
    // Extract email from token
    public String getEmailFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
            .setSigningKey(getSigningKey())
            .build()
            .parseClaimsJws(token)
            .getBody();
        return claims.getSubject();
    }
    
    // Validate token
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }
}
```

**2. Authentication Filter (`JwtAuthenticationFilter.java`)**
```java
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, 
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        // Extract JWT from Authorization header
        String jwt = getJwtFromRequest(request);
        
        // Validate and set authentication
        if (StringUtils.hasText(jwt) && jwtUtil.validateToken(jwt)) {
            String email = jwtUtil.getEmailFromToken(jwt);
            UserDetails userDetails = userDetailsService.loadUserByUsername(email);
            
            UsernamePasswordAuthenticationToken authentication = 
                new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities()
                );
            
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        
        filterChain.doFilter(request, response);
    }
    
    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
```

**3. Auth Controller (`AuthController.java`)**
```java
@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = {"http://localhost:4200", "http://localhost:4201"})
public class AuthController {
    
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest) {
        // Find user by email
        Optional<User> userOpt = userRepository.findByEmailIgnoreCase(
            loginRequest.getUsername().trim()
        );
        
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new MessageResponse("Invalid email or password"));
        }
        
        User user = userOpt.get();
        
        // Validate password (currently without encoding)
        if (!user.getPasswordHash().equals(loginRequest.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new MessageResponse("Invalid email or password"));
        }
        
        // Generate JWT token
        String jwt = jwtUtil.generateToken(user.getEmail());
        
        // Return response with token
        return ResponseEntity.ok(new JwtResponse(
            jwt,
            user.getId(),
            user.getEmail(),
            user.getUuid()
        ));
    }
    
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody SignupRequest signupRequest) {
        String email = signupRequest.getEmail().trim();
        
        // Check if user already exists
        if (userRepository.existsByEmailIgnoreCase(email)) {
            return ResponseEntity.badRequest()
                .body(new MessageResponse("Email is already taken!"));
        }
        
        // Create new user
        User user = new User();
        user.setEmail(email);
        user.setDisplayName(signupRequest.getUsername());
        user.setPasswordHash(signupRequest.getPassword());
        user.setRole(User.Role.USER);
        
        userRepository.save(user);
        
        // Generate JWT
        String jwt = jwtUtil.generateToken(user.getEmail());
        
        return ResponseEntity.ok(new JwtResponse(
            jwt,
            user.getId(),
            user.getEmail(),
            user.getUuid()
        ));
    }
    
    @PostMapping("/guest/onboard")
    public ResponseEntity<?> onboardGuest(@RequestBody Map<String, String> request) {
        String displayName = request.get("displayName");
        
        // Create guest user
        User user = new User();
        user.setDisplayName(displayName);
        user.setEmail("guest_" + UUID.randomUUID() + "@temp.com");
        user.setRole(User.Role.GUEST);
        user.setUuid(UUID.randomUUID().toString());
        
        userRepository.save(user);
        
        String jwt = jwtUtil.generateToken(user.getEmail());
        
        Map<String, String> response = new HashMap<>();
        response.put("uuid", user.getUuid());
        response.put("username", user.getDisplayName());
        response.put("accessToken", jwt);
        response.put("tokenType", "Bearer");
        
        return ResponseEntity.ok(response);
    }
}
```

#### **Frontend Implementation**

**1. Auth Service (`auth.service.ts`)**
```typescript
@Injectable({ providedIn: 'root' })
export class AuthService {
  private baseUrl = 'http://localhost:8081/api/auth';
  private currentUserSubject = new BehaviorSubject<any | null>(this.getStoredUser());
  currentUser$ = this.currentUserSubject.asObservable();
  
  constructor(private http: HttpClient) {
    this.initializeFromStorage();
  }
  
  // Login method
  login(email: string, password: string): Observable<any> {
    return this.http.post(`${this.baseUrl}/login`, { 
      username: email, 
      password 
    }).pipe(
      tap((response: any) => {
        // Store token and user
        this.setToken(response.accessToken);
        this.setUser({
          id: response.userId,
          email: response.username,
          displayName: response.username,
          role: 'USER'
        });
      })
    );
  }
  
  // Register method
  register(userData: any): Observable<any> {
    return this.http.post(`${this.baseUrl}/signup`, {
      username: userData.username,
      email: userData.email,
      password: userData.password
    }).pipe(
      tap((response: any) => {
        this.setToken(response.accessToken);
        this.setUser({
          id: response.userId,
          email: response.username,
          displayName: response.username,
          role: 'USER'
        });
      })
    );
  }
  
  // Guest mode
  setGuestMode(displayName: string): void {
    const guestUser = {
      displayName: displayName,
      isGuest: true,
      role: 'GUEST'
    };
    this.setUser(guestUser);
  }
  
  // Token management
  setToken(token: string) {
    localStorage.setItem('token', token);
  }
  
  getToken(): string | null {
    return localStorage.getItem('token');
  }
  
  // User management
  setUser(user: any | null) {
    if (user) {
      localStorage.setItem('user', JSON.stringify(user));
    } else {
      localStorage.removeItem('user');
      localStorage.removeItem('token');
    }
    this.currentUserSubject.next(user);
  }
  
  getStoredUser() {
    const stored = localStorage.getItem('user');
    if (!stored) return null;
    try {
      return JSON.parse(stored);
    } catch {
      return null;
    }
  }
  
  isLoggedIn(): boolean {
    return !!this.getToken() && !!this.getStoredUser();
  }
  
  logout() {
    this.setUser(null);
  }
}
```

**2. Auth Interceptor (`auth.interceptor.ts`)**
```typescript
export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const authService = inject(AuthService);
  const token = authService.getToken();
  
  // Add token to all requests
  if (token) {
    req = req.clone({
      setHeaders: {
        Authorization: `Bearer ${token}`
      }
    });
  }
  
  return next(req);
};
```

**3. Auth Guard (`auth.guard.ts`)**
```typescript
export const authGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthService);
  const router = inject(Router);
  
  if (authService.isLoggedIn()) {
    return true;
  } else {
    router.navigate(['/login']);
    return false;
  }
};
```

#### **Key Features**
- ✅ JWT-based stateless authentication
- ✅ 7-day token expiration
- ✅ Automatic token refresh on page reload
- ✅ Guest mode without registration
- ✅ Password validation (ready for encryption)
- ✅ Email-based user identification
- ✅ Role-based access control (USER, GUEST, ADMIN)

---

## 📊 Progress Tracking System

### **Implementation Overview**

The progress tracking system monitors user activities across algorithms, quizzes, and exercises.

#### **Backend Implementation**

**1. Entity Model (`UserProgress.java`)**
```java
@Entity
@Table(name = "user_progress")
public class UserProgress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    
    @ManyToOne
    @JoinColumn(name = "algorithm_id")
    private Algorithm algorithm;
    
    private Integer timeSpent;          // Seconds
    private Integer stepsCompleted;
    private Boolean isCompleted;
    private Integer quizScore;
    private Integer codeScore;
    private LocalDateTime startedAt;
    private LocalDateTime completedAt;
    
    // Getters and setters...
}
```

**2. Progress Controller (`ProgressController.java`)**
```java
@RestController
@RequestMapping("/api/progress")
@CrossOrigin(origins = {"http://localhost:4200", "http://localhost:4201"})
public class ProgressController {
    
    @PostMapping("/save")
    public ResponseEntity<?> saveProgress(
        @RequestBody Map<String, Object> progressData,
        @RequestHeader(value = "Authorization", required = false) String authHeader
    ) {
        User user = getUserFromToken(authHeader);
        if (user == null) {
            return ResponseEntity.status(401).body("Unauthorized");
        }
        
        // Extract data
        String algorithmId = (String) progressData.get("algorithmId");
        Integer timeSpent = parseInteger(progressData.get("timeSpent"));
        Integer stepsCompleted = parseInteger(progressData.get("stepsCompleted"));
        Boolean isCompleted = (Boolean) progressData.get("isCompleted");
        Integer quizScore = parseInteger(progressData.get("quizScore"));
        
        // Find or create progress
        Algorithm algorithm = algorithmRepository.findByName(algorithmId)
            .orElse(null);
        
        UserProgress progress = progressRepository
            .findByUserAndAlgorithm(user, algorithm)
            .orElse(new UserProgress());
        
        // Update fields
        progress.setUser(user);
        progress.setAlgorithm(algorithm);
        progress.setTimeSpent(timeSpent);
        progress.setStepsCompleted(stepsCompleted);
        progress.setIsCompleted(isCompleted);
        progress.setQuizScore(quizScore);
        
        if (isCompleted && progress.getCompletedAt() == null) {
            progress.setCompletedAt(LocalDateTime.now());
        }
        
        progressRepository.save(progress);
        
        return ResponseEntity.ok(Map.of("message", "Progress saved successfully"));
    }
    
    @GetMapping("/user")
    public ResponseEntity<?> getUserProgress(
        @RequestHeader(value = "Authorization", required = false) String authHeader
    ) {
        User user = getUserFromToken(authHeader);
        if (user == null) {
            return ResponseEntity.ok(createDefaultUserProgress());
        }
        
        List<UserProgress> progressList = progressRepository.findByUser(user);
        
        Map<String, Object> response = new HashMap<>();
        response.put("userId", user.getId());
        response.put("algorithmsCompleted", progressList.stream()
            .filter(UserProgress::getIsCompleted)
            .map(p -> p.getAlgorithm().getName())
            .collect(Collectors.toList()));
        response.put("totalAlgorithms", progressList.size());
        response.put("completedAlgorithms", (int) progressList.stream()
            .filter(UserProgress::getIsCompleted).count());
        response.put("averageQuizScore", progressList.stream()
            .filter(p -> p.getQuizScore() != null)
            .mapToInt(UserProgress::getQuizScore)
            .average()
            .orElse(0.0));
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/leaderboard")
    public ResponseEntity<?> getLeaderboard() {
        List<User> users = userRepository.findAll();
        
        List<Map<String, Object>> leaderboard = users.stream()
            .map(user -> {
                List<UserProgress> progress = progressRepository.findByUser(user);
                int totalScore = progress.stream()
                    .mapToInt(p -> p.getQuizScore() != null ? p.getQuizScore() : 0)
                    .sum();
                
                Map<String, Object> entry = new HashMap<>();
                entry.put("username", user.getDisplayName());
                entry.put("totalScore", totalScore);
                entry.put("completedAlgorithms", progress.stream()
                    .filter(UserProgress::getIsCompleted).count());
                
                return entry;
            })
            .sorted((a, b) -> Integer.compare(
                (Integer) b.get("totalScore"), 
                (Integer) a.get("totalScore")
            ))
            .collect(Collectors.toList());
        
        return ResponseEntity.ok(leaderboard);
    }
}
```

#### **Frontend Implementation**

**1. Progress Service (`progress.service.ts`)**
```typescript
@Injectable({ providedIn: 'root' })
export class ProgressService {
  private baseUrl = 'http://localhost:8081/api/progress';
  private progressSubject = new BehaviorSubject<any>(null);
  progress$ = this.progressSubject.asObservable();
  
  constructor(
    private http: HttpClient,
    private authService: AuthService
  ) {}
  
  // Save progress to backend
  saveProgress(progressData: any): Observable<any> {
    return this.http.post(`${this.baseUrl}/save`, progressData).pipe(
      tap(() => {
        this.saveLocalProgress(progressData);
      })
    );
  }
  
  // Save locally (for guest users or offline)
  saveLocalProgress(progressData: any): void {
    const user = this.authService.getStoredUser();
    if (!user) return;
    
    const key = `progress_${user.email || user.displayName}`;
    const existing = JSON.parse(localStorage.getItem(key) || '{}');
    
    existing[progressData.algorithmId] = {
      ...progressData,
      timestamp: new Date().toISOString()
    };
    
    localStorage.setItem(key, JSON.stringify(existing));
  }
  
  // Get user progress
  getUserProgress(): Observable<any> {
    return this.http.get(`${this.baseUrl}/user`).pipe(
      tap(progress => this.progressSubject.next(progress))
    );
  }
  
  // Update algorithm completion
  updateAlgorithmCompletion(algorithmId: string, quizScore: number): Observable<any> {
    const user = this.authService.getStoredUser();
    const key = `quiz_progress_${user.email || user.displayName}`;
    const existing = JSON.parse(localStorage.getItem(key) || '{}');
    
    const status = quizScore === 100 ? 'green' : 
                   quizScore >= 60 ? 'yellow' : 'red';
    
    existing[algorithmId] = {
      score: quizScore,
      status: status,
      completed: quizScore === 100,
      timestamp: new Date().toISOString()
    };
    
    localStorage.setItem(key, JSON.stringify(existing));
    
    // Trigger event for real-time updates
    window.dispatchEvent(new CustomEvent('progressUpdated', {
      detail: {
        userIdentifier: user.email || user.displayName,
        progress: existing
      }
    }));
    
    return this.http.post(`${this.baseUrl}/save`, {
      algorithmId,
      quizScore,
      isCompleted: quizScore === 100
    });
  }
}
```

**2. Progress Component (`progress.component.ts`)**
```typescript
export class ProgressComponent implements OnInit {
  dsaProgress = 0;
  daaProgress = 0;
  overallProgress = 0;
  
  realTimeAlgorithms: any[] = [
    { id: 'bubble-sort', name: 'Bubble Sort', progress: 0, status: 'none' },
    { id: 'quick-sort', name: 'Quick Sort', progress: 0, status: 'none' },
    { id: 'merge-sort', name: 'Merge Sort', progress: 0, status: 'none' },
    // ... more algorithms
  ];
  
  ngOnInit(): void {
    this.loadUserProgress();
  }
  
  loadUserProgress(): void {
    const user = this.authService.getStoredUser();
    if (!user || user.isGuest) return;
    
    const userIdentifier = user.email || user.displayName;
    const progressKey = `quiz_progress_${userIdentifier}`;
    const storedProgress = localStorage.getItem(progressKey);
    
    if (storedProgress) {
      const progress = JSON.parse(storedProgress);
      this.calculateRealTimeProgress(progress);
    }
  }
  
  calculateRealTimeProgress(progress: any): void {
    let completedCount = 0;
    let totalScore = 0;
    
    this.realTimeAlgorithms.forEach(algo => {
      const algoProgress = progress[algo.id];
      if (algoProgress) {
        if (algoProgress.status === 'green') {
          algo.progress = 100;
          algo.status = 'green';
          completedCount++;
          totalScore += 100;
        } else if (algoProgress.status === 'yellow') {
          algo.progress = 50;
          algo.status = 'yellow';
        }
      }
    });
    
    this.overallProgress = Math.round(
      (completedCount / this.realTimeAlgorithms.length) * 100
    );
    this.dsaProgress = this.calculateCategoryProgress('dsa', progress);
    this.daaProgress = this.calculateCategoryProgress('daa', progress);
  }
}
```

#### **Key Features**
- ✅ Real-time progress tracking
- ✅ Local storage for offline support
- ✅ Server synchronization
- ✅ Algorithm completion status (green, yellow, red)
- ✅ Time spent tracking
- ✅ Quiz score integration
- ✅ Progress events for navbar updates

---

## 🎯 Quiz System

### **Implementation Overview**

Interactive MCQ quizzes after algorithm visualization completion with instant feedback and scoring.

#### **Backend Implementation**

**1. Quiz Entity (`Quiz.java`)**
```java
@Entity
@Table(name = "quizzes")
public class Quiz {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "algorithm_id")
    private Algorithm algorithm;
    
    private String question;
    private String optionA;
    private String optionB;
    private String optionC;
    private String optionD;
    private String correctAnswer;  // A, B, C, or D
    private String explanation;
    private String difficulty;     // EASY, MEDIUM, HARD
    
    // Getters and setters...
}
```

**2. Quiz Controller (`QuizController.java`)**
```java
@RestController
@RequestMapping("/api/quiz")
@CrossOrigin(origins = "*", maxAge = 3600)
public class QuizController {
    
    @Autowired
    private QuizService quizService;
    
    @GetMapping("/public/algorithm/{algorithmId}")
    public ResponseEntity<List<Quiz>> getQuizzesByAlgorithm(
        @PathVariable Long algorithmId
    ) {
        List<Quiz> quizzes = quizService.getQuizzesByAlgorithm(algorithmId);
        return ResponseEntity.ok(quizzes);
    }
    
    @GetMapping("/public/algorithm/{algorithmId}/random/{limit}")
    public ResponseEntity<List<Quiz>> getRandomQuizzesByAlgorithm(
        @PathVariable Long algorithmId,
        @PathVariable int limit
    ) {
        List<Quiz> quizzes = quizService.getRandomQuizzesByAlgorithm(algorithmId, limit);
        return ResponseEntity.ok(quizzes);
    }
    
    @PostMapping("/submit")
    public ResponseEntity<QuizAttempt> submitQuizAnswer(
        @RequestBody Map<String, Object> request,
        Authentication authentication
    ) {
        String email = authentication.getName();
        User user = userRepository.findByEmailIgnoreCase(email)
            .orElseThrow(() -> new RuntimeException("User not found"));
        
        Long quizId = Long.parseLong(request.get("quizId").toString());
        String userAnswer = request.get("userAnswer").toString();
        
        Quiz quiz = quizService.getQuizById(quizId)
            .orElseThrow(() -> new RuntimeException("Quiz not found"));
        
        boolean isCorrect = quiz.getCorrectAnswer().equalsIgnoreCase(userAnswer);
        
        // Create attempt record
        QuizAttempt attempt = new QuizAttempt();
        attempt.setUser(user);
        attempt.setQuiz(quiz);
        attempt.setUserAnswer(userAnswer);
        attempt.setIsCorrect(isCorrect);
        attempt.setAttemptedAt(LocalDateTime.now());
        
        quizAttemptRepository.save(attempt);
        
        return ResponseEntity.ok(attempt);
    }
    
    @GetMapping("/attempts/{userId}")
    public ResponseEntity<List<QuizAttempt>> getUserAttempts(
        @PathVariable Long userId
    ) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));
        
        List<QuizAttempt> attempts = quizAttemptRepository.findByUser(user);
        return ResponseEntity.ok(attempts);
    }
}
```

#### **Frontend Implementation**

**1. Quiz Service (`quiz.service.ts`)**
```typescript
@Injectable({ providedIn: 'root' })
export class QuizService {
  private baseUrl = 'http://localhost:8080/api/quiz';
  private currentQuizSubject = new BehaviorSubject<QuizQuestion[] | null>(null);
  private quizResultSubject = new BehaviorSubject<QuizResult | null>(null);
  
  currentQuiz$ = this.currentQuizSubject.asObservable();
  quizResult$ = this.quizResultSubject.asObservable();
  
  // Load quiz questions for algorithm
  loadQuiz(algorithmId: string): Observable<QuizQuestion[]> {
    return this.http.get<QuizQuestion[]>(`${this.baseUrl}/${algorithmId}`).pipe(
      tap(questions => {
        this.currentQuizSubject.next(questions);
      })
    );
  }
  
  // Submit quiz answer
  submitAnswer(quizId: number, userAnswer: string): Observable<any> {
    return this.http.post(`${this.baseUrl}/submit`, {
      quizId,
      userAnswer
    });
  }
  
  // Calculate quiz score
  calculateScore(questions: QuizQuestion[], answers: number[]): QuizResult {
    let correctCount = 0;
    
    questions.forEach((q, index) => {
      if (answers[index] === q.correctAnswer) {
        correctCount++;
      }
    });
    
    const percentage = (correctCount / questions.length) * 100;
    
    return {
      score: correctCount,
      total: questions.length,
      percentage: Math.round(percentage),
      passed: percentage >= 60
    };
  }
  
  // Submit full quiz result
  submitQuizResult(result: QuizResult): Observable<any> {
    return this.http.post(`${this.baseUrl}/result`, result).pipe(
      tap(() => {
        this.quizResultSubject.next(result);
      })
    );
  }
}
```

**2. Algorithm Skeleton Component (Quiz Modal)**
```typescript
export class AlgorithmSkeletonComponent {
  showQuizModal = false;
  currentQuizQuestions: any[] = [];
  currentQuizIndex = 0;
  selectedOption: number | null = null;
  showAnswer = false;
  correctAnswers = 0;
  showQuizResults = false;
  quizScore = 0;
  
  // Open quiz
  openQuiz(): void {
    if (!this.canTakeQuiz) return;
    
    this.showQuizModal = true;
    this.currentQuizIndex = 0;
    this.selectedOption = null;
    this.correctAnswers = 0;
    
    // Shuffle questions
    this.currentQuizQuestions = this.shuffleArray([...this.quizQuestions]);
  }
  
  // Select answer option
  selectOption(index: number): void {
    if (this.showAnswer) return;
    this.selectedOption = index;
  }
  
  // Submit or go to next question
  handleQuizAction(): void {
    if (!this.showAnswer) {
      // Submit answer
      this.showAnswer = true;
      const correctIndex = this.currentQuizQuestions[this.currentQuizIndex].correctAnswer;
      
      if (this.selectedOption === correctIndex) {
        this.correctAnswers++;
      }
    } else {
      // Next question
      this.nextQuestion();
    }
  }
  
  // Navigate to next question
  nextQuestion(): void {
    if (this.currentQuizIndex < this.currentQuizQuestions.length - 1) {
      this.currentQuizIndex++;
      this.selectedOption = null;
      this.showAnswer = false;
    } else {
      this.finishQuiz();
    }
  }
  
  // Finish and show results
  finishQuiz(): void {
    this.quizScore = Math.round(
      (this.correctAnswers / this.currentQuizQuestions.length) * 100
    );
    
    // Determine completion status
    if (this.quizScore === 100) {
      this.completionStatus = 'green';
      this.quizFeedback = 'Perfect! You mastered this algorithm!';
    } else if (this.quizScore >= 60) {
      this.completionStatus = 'yellow';
      this.quizFeedback = 'Good job! Review and try again for 100%.';
    } else {
      this.completionStatus = 'review';
      this.quizFeedback = 'Keep learning! Review the algorithm and retry.';
    }
    
    this.showQuizResults = true;
    this.saveQuizProgress();
  }
  
  // Save quiz progress
  saveQuizProgress(): void {
    const user = this.authService.getStoredUser();
    if (!user) return;
    
    const userIdentifier = user.email || user.displayName;
    const progressKey = `quiz_progress_${userIdentifier}`;
    const existingProgress = JSON.parse(
      localStorage.getItem(progressKey) || '{}'
    );
    
    existingProgress[this.algorithmId] = {
      score: this.quizScore,
      status: this.completionStatus,
      completed: this.quizScore === 100,
      timestamp: new Date().toISOString()
    };
    
    localStorage.setItem(progressKey, JSON.stringify(existingProgress));
    
    // Trigger progress update event
    window.dispatchEvent(new CustomEvent('progressUpdated', {
      detail: { userIdentifier, progress: existingProgress }
    }));
  }
  
  // Retry quiz
  retryQuiz(): void {
    this.showQuizResults = false;
    this.currentQuizIndex = 0;
    this.selectedOption = null;
    this.showAnswer = false;
    this.correctAnswers = 0;
    
    // Reshuffle questions
    this.currentQuizQuestions = this.shuffleArray([...this.quizQuestions]);
  }
}
```

#### **Quiz Features**
- ✅ Algorithm-specific questions
- ✅ Multiple choice (4 options)
- ✅ Instant feedback with explanations
- ✅ Score calculation (green: 100%, yellow: 60-99%, review: <60%)
- ✅ Question shuffling for variety
- ✅ Retry mechanism
- ✅ Progress integration
- ✅ Real-time updates to navbar
- ✅ Local storage persistence

---

## 💻 Code Editor System

### **Implementation Overview**

Monaco Editor integration for multi-language code editing and execution.

#### **Frontend Implementation**

**1. Monaco Editor Component (`code-editor.component.ts`)**
```typescript
import * as monaco from 'monaco-editor';

export class CodeEditorComponent implements OnInit, AfterViewInit {
  @ViewChild('editorContainer', { static: false }) 
  editorContainer!: ElementRef;
  
  private editor: monaco.editor.IStandaloneCodeEditor | null = null;
  
  selectedLanguage = 'javascript';
  code = '';
  output = '';
  isExecuting = false;
  
  languageOptions = [
    { value: 'javascript', label: 'JavaScript' },
    { value: 'python', label: 'Python' },
    { value: 'java', label: 'Java' },
    { value: 'cpp', label: 'C++' }
  ];
  
  codeTemplates = {
    javascript: `// JavaScript Code
function bubbleSort(arr) {
  const n = arr.length;
  for (let i = 0; i < n - 1; i++) {
    for (let j = 0; j < n - i - 1; j++) {
      if (arr[j] > arr[j + 1]) {
        [arr[j], arr[j + 1]] = [arr[j + 1], arr[j]];
      }
    }
  }
  return arr;
}

console.log(bubbleSort([64, 34, 25, 12, 22, 11, 90]));`,
    
    python: `# Python Code
def bubble_sort(arr):
    n = len(arr)
    for i in range(n - 1):
        for j in range(n - i - 1):
            if arr[j] > arr[j + 1]:
                arr[j], arr[j + 1] = arr[j + 1], arr[j]
    return arr

print(bubble_sort([64, 34, 25, 12, 22, 11, 90]))`,
    
    java: `// Java Code
public class BubbleSort {
    public static void bubbleSort(int[] arr) {
        int n = arr.length;
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (arr[j] > arr[j + 1]) {
                    int temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                }
            }
        }
    }
}`,
    
    cpp: `// C++ Code
#include <iostream>
#include <vector>
using namespace std;

void bubbleSort(vector<int>& arr) {
    int n = arr.size();
    for (int i = 0; i < n - 1; i++) {
        for (int j = 0; j < n - i - 1; j++) {
            if (arr[j] > arr[j + 1]) {
                swap(arr[j], arr[j + 1]);
            }
        }
    }
}`
  };
  
  ngAfterViewInit(): void {
    this.initializeEditor();
  }
  
  initializeEditor(): void {
    if (!this.editorContainer) return;
    
    this.editor = monaco.editor.create(
      this.editorContainer.nativeElement,
      {
        value: this.codeTemplates[this.selectedLanguage],
        language: this.selectedLanguage,
        theme: 'vs-dark',
        automaticLayout: true,
        minimap: { enabled: true },
        fontSize: 14,
        lineNumbers: 'on',
        roundedSelection: true,
        scrollBeyondLastLine: false,
        readOnly: false,
        cursorStyle: 'line',
        wordWrap: 'on'
      }
    );
    
    // Listen to editor changes
    this.editor.onDidChangeModelContent(() => {
      this.code = this.editor?.getValue() || '';
    });
  }
  
  changeLanguage(language: string): void {
    this.selectedLanguage = language;
    
    if (this.editor) {
      const model = this.editor.getModel();
      if (model) {
        monaco.editor.setModelLanguage(model, language);
        this.editor.setValue(this.codeTemplates[language]);
      }
    }
  }
  
  runCode(): void {
    this.isExecuting = true;
    this.output = 'Executing code...';
    
    const codeToExecute = this.editor?.getValue() || '';
    
    this.codeExecutionService.executeCode(
      codeToExecute,
      this.selectedLanguage
    ).subscribe({
      next: (result) => {
        this.output = result.output || 'Code executed successfully!';
        this.isExecuting = false;
      },
      error: (error) => {
        this.output = `Error: ${error.message || 'Execution failed'}`;
        this.isExecuting = false;
      }
    });
  }
  
  clearCode(): void {
    if (this.editor) {
      this.editor.setValue('');
    }
    this.output = '';
  }
  
  resetCode(): void {
    if (this.editor) {
      this.editor.setValue(this.codeTemplates[this.selectedLanguage]);
    }
    this.output = '';
  }
  
  ngOnDestroy(): void {
    if (this.editor) {
      this.editor.dispose();
    }
  }
}
```

**2. Code Execution Service (`code-execution.service.ts`)**
```typescript
@Injectable({ providedIn: 'root' })
export class CodeExecutionService {
  private baseUrl = 'http://localhost:8081/api/code';
  
  executeCode(code: string, language: string): Observable<any> {
    return this.http.post(`${this.baseUrl}/execute`, {
      code,
      language
    }).pipe(
      catchError(error => {
        console.error('Code execution error:', error);
        return throwError(() => error);
      })
    );
  }
}
```

#### **Backend Implementation**

**Code Execution Controller (`CodeExecutionController.java`)**
```java
@RestController
@RequestMapping("/api/code")
@CrossOrigin(origins = {"http://localhost:4200", "http://localhost:4201"})
public class CodeExecutionController {
    
    @PostMapping("/execute")
    public ResponseEntity<?> executeCode(@RequestBody Map<String, String> request) {
        String code = request.get("code");
        String language = request.get("language");
        
        try {
            String output = executeCodeInSandbox(code, language);
            
            return ResponseEntity.ok(Map.of(
                "output", output,
                "status", "success"
            ));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of(
                "error", e.getMessage(),
                "status", "error"
            ));
        }
    }
    
    private String executeCodeInSandbox(String code, String language) {
        // Mock implementation - in production, use Docker containers
        // or services like Judge0, Piston, etc.
        
        switch (language.toLowerCase()) {
            case "javascript":
                return "Mock JavaScript Output: [11, 12, 22, 25, 34, 64, 90]";
            case "python":
                return "Mock Python Output: [11, 12, 22, 25, 34, 64, 90]";
            case "java":
                return "Mock Java Output: [11, 12, 22, 25, 34, 64, 90]";
            case "cpp":
                return "Mock C++ Output: [11, 12, 22, 25, 34, 64, 90]";
            default:
                throw new RuntimeException("Unsupported language");
        }
    }
}
```

#### **Key Features**
- ✅ Monaco Editor (VS Code engine)
- ✅ Multi-language support (JS, Python, Java, C++)
- ✅ Syntax highlighting
- ✅ Code templates
- ✅ Real-time output display
- ✅ Error handling
- ✅ Code execution (mock implementation)
- ✅ Clear and reset functions

---

## 📈 Leaderboard System

### **Implementation Overview**

Competitive ranking system based on quiz scores and algorithm completions.

#### **Backend Implementation**

```java
@RestController
@RequestMapping("/api/leaderboard")
@CrossOrigin(origins = {"http://localhost:4200", "http://localhost:4201"})
public class LeaderboardController {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private UserProgressRepository progressRepository;
    
    @GetMapping("/top")
    public ResponseEntity<List<LeaderboardEntry>> getTopUsers(
        @RequestParam(defaultValue = "50") int limit
    ) {
        List<User> users = userRepository.findAll();
        
        List<LeaderboardEntry> leaderboard = users.stream()
            .filter(user -> user.getRole() != User.Role.GUEST)
            .map(user -> {
                List<UserProgress> progress = progressRepository.findByUser(user);
                
                int totalScore = progress.stream()
                    .mapToInt(p -> p.getQuizScore() != null ? p.getQuizScore() : 0)
                    .sum();
                
                long completedAlgorithms = progress.stream()
                    .filter(UserProgress::getIsCompleted)
                    .count();
                
                return new LeaderboardEntry(
                    user.getId(),
                    user.getDisplayName(),
                    totalScore,
                    (int) completedAlgorithms
                );
            })
            .sorted((a, b) -> Integer.compare(b.getTotalScore(), a.getTotalScore()))
            .limit(limit)
            .collect(Collectors.toList());
        
        // Assign ranks
        for (int i = 0; i < leaderboard.size(); i++) {
            leaderboard.get(i).setRank(i + 1);
        }
        
        return ResponseEntity.ok(leaderboard);
    }
    
    @GetMapping("/user/{userId}")
    public ResponseEntity<LeaderboardEntry> getUserRanking(
        @PathVariable Long userId
    ) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));
        
        List<UserProgress> progress = progressRepository.findByUser(user);
        
        int totalScore = progress.stream()
            .mapToInt(p -> p.getQuizScore() != null ? p.getQuizScore() : 0)
            .sum();
        
        long completedAlgorithms = progress.stream()
            .filter(UserProgress::getIsCompleted)
            .count();
        
        // Calculate rank
        List<User> allUsers = userRepository.findAll();
        int rank = (int) allUsers.stream()
            .filter(u -> {
                List<UserProgress> uProgress = progressRepository.findByUser(u);
                int uScore = uProgress.stream()
                    .mapToInt(p -> p.getQuizScore() != null ? p.getQuizScore() : 0)
                    .sum();
                return uScore > totalScore;
            })
            .count() + 1;
        
        LeaderboardEntry entry = new LeaderboardEntry(
            user.getId(),
            user.getDisplayName(),
            totalScore,
            (int) completedAlgorithms
        );
        entry.setRank(rank);
        
        return ResponseEntity.ok(entry);
    }
}
```

#### **Frontend Implementation**

```typescript
export class LeaderboardComponent implements OnInit {
  leaderboardData: any[] = [];
  userRank: number = 0;
  currentUser: any;
  
  ngOnInit(): void {
    this.loadLeaderboard();
    this.loadCurrentUser();
  }
  
  loadLeaderboard(): void {
    this.leaderboardService.getTopUsers(50).subscribe({
      next: (data) => {
        this.leaderboardData = data;
      },
      error: (error) => {
        console.error('Error loading leaderboard:', error);
      }
    });
  }
  
  loadCurrentUser(): void {
    const user = this.authService.getStoredUser();
    if (user && !user.isGuest) {
      this.currentUser = user;
      this.leaderboardService.getUserRanking(user.id).subscribe({
        next: (ranking) => {
          this.userRank = ranking.rank;
        }
      });
    }
  }
}
```

#### **Key Features**
- ✅ Top 50 users ranking
- ✅ Score-based sorting
- ✅ Algorithm completion count
- ✅ User rank calculation
- ✅ Real-time updates
- ✅ Guest exclusion
- ✅ Rank badges for top 10

---

## 🎨 Visualization System (The Core Feature!)

### **Technology Stack**

The visualization system is the heart of the application, using powerful animation libraries:

- **D3.js 7.9.0** - Data-driven DOM manipulation and SVG creation
- **GSAP 3.13.0** - Professional-grade animations with GPU acceleration
- **Chart.js 4.4.4** - Performance graphs and complexity charts
- **Custom CSS Animations** - Keyframe animations for smooth transitions
- **SVG Graphics** - Scalable vector graphics for arrows, nodes, and edges

---

### **Architecture Overview**

Each algorithm visualization follows a consistent pattern:

```
User Action → Algorithm Service → Step Generation → Animation Queue → Visual Update
```

**Key Components:**
1. **Algorithm Skeleton** - Shared wrapper component
2. **Visualization Canvas** - D3.js SVG container
3. **Control Panel** - Play, pause, step, speed controls
4. **Audio Service** - Sound effects for actions
5. **Step Generator** - Creates animation steps
6. **Animation Engine** - GSAP timeline management

---

### **1. Linked List Visualization**

#### **Implementation (`linked-list.component.ts`)**

**Features:**
- ✅ **Four List Types**: Singly, Doubly, Circular, Doubly-Circular
- ✅ **SVG-Based Arrows**: Professional marker-based arrows
- ✅ **8-Node Circular Limit**: Prevents overcrowding
- ✅ **Responsive Type Switching**: Instant layout updates
- ✅ **Color Coding**: Orange (circular), Purple (doubly-circular)
- ✅ **Professional Animations**: GPU-accelerated with GSAP

**Key Code:**

```typescript
export class LinkedListComponent {
  listType: 'singly' | 'circular' | 'doubly' | 'doubly-circular' = 'singly';
  nodes: any[] = [];
  MAX_CIRCULAR_NODES = 8;
  
  // Node positioning calculation
  calculateNodePositions(): void {
    if (this.listType === 'circular' || this.listType === 'doubly-circular') {
      // Circular layout with increased radius
      const radius = 280; // Increased from 200
      const centerX = 450;
      const centerY = 350;
      const angleStep = (2 * Math.PI) / this.nodes.length;
      
      this.nodes.forEach((node, index) => {
        const angle = index * angleStep - Math.PI / 2;
        node.x = centerX + radius * Math.cos(angle);
        node.y = centerY + radius * Math.sin(angle);
      });
    } else {
      // Linear layout with proper spacing
      const startX = 150;
      const startY = 300;
      const spacing = 200;
      
      this.nodes.forEach((node, index) => {
        node.x = startX + index * spacing;
        node.y = startY;
      });
    }
    
    this.cdr.detectChanges();
  }
  
  // SVG Arrow Creation
  createArrowPath(fromNode: any, toNode: any, isBackward = false): string {
    const dx = toNode.x - fromNode.x;
    const dy = toNode.y - fromNode.y;
    const distance = Math.sqrt(dx * dx + dy * dy);
    
    if (this.listType === 'circular' || this.listType === 'doubly-circular') {
      // Arc path for circular connections
      const radius = 280;
      const largeArcFlag = 0;
      const sweepFlag = isBackward ? 0 : 1;
      
      return `M ${fromNode.x + 40} ${fromNode.y} 
              A ${radius} ${radius} 0 ${largeArcFlag} ${sweepFlag} 
              ${toNode.x - 40} ${toNode.y}`;
    } else {
      // Straight line with bezier curve for doubly
      if (this.listType === 'doubly' && isBackward) {
        const controlOffset = 50;
        return `M ${fromNode.x - 40} ${fromNode.y} 
                C ${fromNode.x - 40} ${fromNode.y - controlOffset}, 
                  ${toNode.x + 40} ${toNode.y - controlOffset}, 
                  ${toNode.x + 40} ${toNode.y}`;
      }
      
      return `M ${fromNode.x + 40} ${fromNode.y} 
              L ${toNode.x - 40} ${toNode.y}`;
    }
  }
  
  // Insert Animation
  insertAtHead(value: number): void {
    if (this.isCircularType() && this.nodes.length >= this.MAX_CIRCULAR_NODES) {
      alert(`Circular lists limited to ${this.MAX_CIRCULAR_NODES} nodes`);
      return;
    }
    
    const newNode = { value, x: 0, y: 0 };
    this.nodes.unshift(newNode);
    
    this.calculateNodePositions();
    this.playSound('insert');
    
    // GSAP Animation
    gsap.from(`#node-0`, {
      scale: 0,
      opacity: 0,
      duration: 0.5,
      ease: 'back.out(1.7)'
    });
    
    this.updateVisualization();
  }
  
  // Delete Animation
  deleteAtHead(): void {
    if (this.nodes.length === 0) return;
    
    // Animate out
    gsap.to(`#node-0`, {
      scale: 0,
      opacity: 0,
      duration: 0.3,
      ease: 'back.in(1.7)',
      onComplete: () => {
        this.nodes.shift();
        this.calculateNodePositions();
        this.updateVisualization();
      }
    });
    
    this.playSound('delete');
  }
  
  // Type switching with responsive layout
  onListTypeChange(): void {
    this.calculateNodePositions();
    this.cdr.detectChanges();
    
    // Animate transition
    gsap.from('.node-box', {
      scale: 0.8,
      opacity: 0.5,
      duration: 0.4,
      stagger: 0.1,
      ease: 'power2.out'
    });
  }
}
```

**CSS Animations:**

```scss
// Circular arrow animation
@keyframes dashFlow {
  0% {
    stroke-dashoffset: 1000;
  }
  100% {
    stroke-dashoffset: 0;
  }
}

.arrow-circular {
  stroke: #ff6b35;
  animation: dashFlow 2s linear infinite;
}

// Bidirectional pulse for doubly-circular
@keyframes bidirectionalPulse {
  0%, 100% {
    stroke-width: 2;
    opacity: 1;
  }
  50% {
    stroke-width: 3;
    opacity: 0.6;
  }
}

.arrow-doubly-circular {
  stroke: #7c3aed;
  animation: bidirectionalPulse 1.5s ease-in-out infinite;
}

// Node insertion animation
@keyframes nodeInsert {
  0% {
    transform: scale(0) rotate(0deg);
    opacity: 0;
  }
  60% {
    transform: scale(1.2) rotate(360deg);
  }
  100% {
    transform: scale(1) rotate(360deg);
    opacity: 1;
  }
}

// HEAD pointer animation
.head-pointer {
  animation: bounce 1s ease-in-out infinite;
}

@keyframes bounce {
  0%, 100% {
    transform: translateY(0);
  }
  50% {
    transform: translateY(-10px);
  }
}
```

**SVG Marker Definitions:**

```html
<svg>
  <defs>
    <!-- Forward arrow marker -->
    <marker id="arrowhead-forward" markerWidth="10" markerHeight="10" 
            refX="2" refY="3" orient="auto">
      <polygon points="0 0, 6 3, 0 6" fill="#007bff" />
    </marker>
    
    <!-- Backward arrow marker (blue) -->
    <marker id="arrowhead-backward" markerWidth="10" markerHeight="10" 
            refX="2" refY="3" orient="auto">
      <polygon points="0 0, 6 3, 0 6" fill="#0056b3" />
    </marker>
    
    <!-- Circular arrow marker (orange) -->
    <marker id="arrowhead-circular" markerWidth="10" markerHeight="10" 
            refX="2" refY="3" orient="auto">
      <polygon points="0 0, 6 3, 0 6" fill="#ff6b35" />
    </marker>
    
    <!-- Doubly-circular marker (purple) -->
    <marker id="arrowhead-doubly-circular" markerWidth="10" markerHeight="10" 
            refX="2" refY="3" orient="auto">
      <polygon points="0 0, 6 3, 0 6" fill="#7c3aed" />
    </marker>
  </defs>
  
  <!-- Dynamic arrows -->
  <path *ngFor="let node of nodes; let i = index"
        [attr.d]="createArrowPath(node, nodes[i + 1])"
        [attr.marker-end]="'url(#arrowhead-' + listType + ')'"
        class="arrow-{{listType}}" />
</svg>
```

---

### **2. Sorting Algorithm Visualizations**

#### **Bubble Sort with D3.js**

```typescript
export class BubbleSortComponent implements AfterViewInit {
  private svg: any;
  private data: number[] = [];
  private isPlaying = false;
  
  ngAfterViewInit(): void {
    this.initializeSVG();
  }
  
  initializeSVG(): void {
    const width = 800;
    const height = 400;
    
    this.svg = d3.select('#visualization')
      .append('svg')
      .attr('width', width)
      .attr('height', height);
  }
  
  visualizeBubbleSort(array: number[]): void {
    this.data = [...array];
    const steps = this.generateBubbleSortSteps(this.data);
    
    this.animateSteps(steps);
  }
  
  generateBubbleSortSteps(arr: number[]): any[] {
    const steps: any[] = [];
    const n = arr.length;
    
    for (let i = 0; i < n - 1; i++) {
      for (let j = 0; j < n - i - 1; j++) {
        // Comparison step
        steps.push({
          type: 'compare',
          indices: [j, j + 1],
          array: [...arr]
        });
        
        if (arr[j] > arr[j + 1]) {
          // Swap step
          [arr[j], arr[j + 1]] = [arr[j + 1], arr[j]];
          steps.push({
            type: 'swap',
            indices: [j, j + 1],
            array: [...arr]
          });
        }
      }
      
      // Mark as sorted
      steps.push({
        type: 'sorted',
        index: n - i - 1,
        array: [...arr]
      });
    }
    
    return steps;
  }
  
  animateSteps(steps: any[]): void {
    const timeline = gsap.timeline();
    
    steps.forEach((step, index) => {
      timeline.add(() => {
        this.renderStep(step);
      }, index * 0.5);
    });
  }
  
  renderStep(step: any): void {
    const barWidth = 60;
    const barSpacing = 10;
    
    // D3.js data binding
    const bars = this.svg.selectAll('rect')
      .data(step.array);
    
    // Enter new bars
    bars.enter()
      .append('rect')
      .attr('x', (d: any, i: number) => i * (barWidth + barSpacing))
      .attr('y', (d: any) => 400 - d * 3)
      .attr('width', barWidth)
      .attr('height', (d: any) => d * 3)
      .attr('fill', '#4a90e2');
    
    // Update existing bars
    bars.transition()
      .duration(300)
      .attr('x', (d: any, i: number) => i * (barWidth + barSpacing))
      .attr('y', (d: any) => 400 - d * 3)
      .attr('height', (d: any) => d * 3)
      .attr('fill', (d: any, i: number) => {
        if (step.type === 'compare' && step.indices.includes(i)) {
          return '#ff6b6b'; // Red for comparison
        } else if (step.type === 'swap' && step.indices.includes(i)) {
          return '#51cf66'; // Green for swap
        } else if (step.type === 'sorted' && i >= step.index) {
          return '#7c3aed'; // Purple for sorted
        }
        return '#4a90e2'; // Default blue
      });
    
    // Exit old bars
    bars.exit().remove();
    
    // Add value labels
    const labels = this.svg.selectAll('text')
      .data(step.array);
    
    labels.enter()
      .append('text')
      .attr('x', (d: any, i: number) => i * (barWidth + barSpacing) + barWidth / 2)
      .attr('y', (d: any) => 400 - d * 3 - 10)
      .attr('text-anchor', 'middle')
      .attr('fill', '#fff')
      .text((d: any) => d);
    
    labels.transition()
      .duration(300)
      .attr('x', (d: any, i: number) => i * (barWidth + barSpacing) + barWidth / 2)
      .attr('y', (d: any) => 400 - d * 3 - 10)
      .text((d: any) => d);
    
    labels.exit().remove();
  }
}
```

---

### **3. Binary Search Tree Visualization**

```typescript
export class BinarySearchTreeComponent {
  private treeData: any = null;
  private svg: any;
  private treeLayout: any;
  
  initializeTree(): void {
    const width = 800;
    const height = 600;
    
    this.svg = d3.select('#tree-container')
      .append('svg')
      .attr('width', width)
      .attr('height', height)
      .append('g')
      .attr('transform', 'translate(40,40)');
    
    // D3 tree layout
    this.treeLayout = d3.tree()
      .size([width - 80, height - 80]);
  }
  
  insertNode(value: number): void {
    if (!this.treeData) {
      this.treeData = { value, children: [] };
    } else {
      this.insertRecursive(this.treeData, value);
    }
    
    this.updateTreeVisualization();
  }
  
  insertRecursive(node: any, value: number): void {
    if (value < node.value) {
      if (!node.children[0]) {
        node.children[0] = { value, children: [] };
      } else {
        this.insertRecursive(node.children[0], value);
      }
    } else {
      if (!node.children[1]) {
        node.children[1] = { value, children: [] };
      } else {
        this.insertRecursive(node.children[1], value);
      }
    }
  }
  
  updateTreeVisualization(): void {
    const root = d3.hierarchy(this.treeData);
    const treeData = this.treeLayout(root);
    
    // Links (edges)
    const links = this.svg.selectAll('.link')
      .data(treeData.links());
    
    links.enter()
      .append('path')
      .attr('class', 'link')
      .merge(links)
      .transition()
      .duration(500)
      .attr('d', d3.linkVertical()
        .x((d: any) => d.x)
        .y((d: any) => d.y)
      )
      .attr('fill', 'none')
      .attr('stroke', '#555')
      .attr('stroke-width', 2);
    
    links.exit().remove();
    
    // Nodes
    const nodes = this.svg.selectAll('.node')
      .data(treeData.descendants());
    
    const nodeEnter = nodes.enter()
      .append('g')
      .attr('class', 'node');
    
    nodeEnter.append('circle')
      .attr('r', 25)
      .attr('fill', '#4a90e2');
    
    nodeEnter.append('text')
      .attr('dy', 5)
      .attr('text-anchor', 'middle')
      .attr('fill', '#fff')
      .text((d: any) => d.data.value);
    
    nodeEnter.merge(nodes)
      .transition()
      .duration(500)
      .attr('transform', (d: any) => `translate(${d.x},${d.y})`);
    
    nodes.exit().remove();
  }
}
```

---

### **4. Graph Algorithm Visualization (Dijkstra's)**

```typescript
export class DijkstraComponent {
  nodes: any[] = [];
  edges: any[] = [];
  
  visualizeDijkstra(startNode: number): void {
    const distances: { [key: number]: number } = {};
    const previous: { [key: number]: number | null } = {};
    const unvisited = new Set(this.nodes.map(n => n.id));
    
    // Initialize distances
    this.nodes.forEach(node => {
      distances[node.id] = node.id === startNode ? 0 : Infinity;
      previous[node.id] = null;
    });
    
    const steps: any[] = [];
    
    while (unvisited.size > 0) {
      // Find node with minimum distance
      let current = null;
      let minDist = Infinity;
      
      unvisited.forEach(nodeId => {
        if (distances[nodeId] < minDist) {
          minDist = distances[nodeId];
          current = nodeId;
        }
      });
      
      if (current === null) break;
      
      steps.push({
        type: 'visit',
        node: current,
        distance: distances[current]
      });
      
      unvisited.delete(current);
      
      // Update neighbors
      const neighbors = this.edges.filter(e => e.source === current);
      
      neighbors.forEach(edge => {
        const alt = distances[current] + edge.weight;
        
        if (alt < distances[edge.target]) {
          distances[edge.target] = alt;
          previous[edge.target] = current;
          
          steps.push({
            type: 'update',
            node: edge.target,
            distance: alt,
            via: current
          });
        }
      });
    }
    
    this.animateDijkstraSteps(steps);
  }
  
  animateDijkstraSteps(steps: any[]): void {
    const timeline = gsap.timeline();
    
    steps.forEach((step, index) => {
      timeline.add(() => {
        if (step.type === 'visit') {
          // Highlight visited node
          gsap.to(`#node-${step.node}`, {
            fill: '#7c3aed',
            scale: 1.2,
            duration: 0.3
          });
          
          // Update distance label
          d3.select(`#distance-${step.node}`)
            .text(step.distance);
        } else if (step.type === 'update') {
          // Highlight edge
          gsap.to(`#edge-${step.via}-${step.node}`, {
            stroke: '#51cf66',
            strokeWidth: 4,
            duration: 0.3
          });
          
          // Update distance
          d3.select(`#distance-${step.node}`)
            .transition()
            .duration(300)
            .style('color', '#51cf66')
            .text(step.distance);
        }
      }, index * 0.8);
    });
  }
}
```

---

### **5. Audio Service Integration**

```typescript
@Injectable({ providedIn: 'root' })
export class SoundService {
  private sounds: { [key: string]: HTMLAudioElement } = {};
  private enabled = true;
  
  constructor() {
    this.loadSounds();
  }
  
  loadSounds(): void {
    this.sounds['insert'] = new Audio('assets/sounds/insert.mp3');
    this.sounds['delete'] = new Audio('assets/sounds/delete.mp3');
    this.sounds['swap'] = new Audio('assets/sounds/swap.mp3');
    this.sounds['compare'] = new Audio('assets/sounds/compare.mp3');
    this.sounds['complete'] = new Audio('assets/sounds/complete.mp3');
    this.sounds['error'] = new Audio('assets/sounds/error.mp3');
  }
  
  play(soundName: string): void {
    if (!this.enabled || !this.sounds[soundName]) return;
    
    this.sounds[soundName].currentTime = 0;
    this.sounds[soundName].play().catch(error => {
      console.warn('Audio play failed:', error);
    });
  }
  
  toggle(): void {
    this.enabled = !this.enabled;
  }
}
```

---

### **6. Algorithm Skeleton (Shared Component)**

```typescript
@Component({
  selector: 'app-algorithm-skeleton',
  templateUrl: './algorithm-skeleton.component.html',
  standalone: true
})
export class AlgorithmSkeletonComponent {
  @Input() algorithmId!: string;
  @Input() algorithmName!: string;
  @Input() category: 'dsa' | 'daa' = 'dsa';
  @Input() difficulty: 'easy' | 'medium' | 'hard' = 'medium';
  @Input() timeComplexity!: string;
  @Input() spaceComplexity!: string;
  @Input() description!: string;
  @Input() quizQuestions: any[] = [];
  
  isPlaying = false;
  isPaused = false;
  speed = 1;
  currentStep = 0;
  
  // Control methods
  play(): void {
    this.isPlaying = true;
    this.isPaused = false;
  }
  
  pause(): void {
    this.isPaused = true;
  }
  
  stop(): void {
    this.isPlaying = false;
    this.isPaused = false;
    this.currentStep = 0;
  }
  
  stepForward(): void {
    if (this.currentStep < this.totalSteps) {
      this.currentStep++;
    }
  }
  
  stepBackward(): void {
    if (this.currentStep > 0) {
      this.currentStep--;
    }
  }
  
  changeSpeed(speed: number): void {
    this.speed = speed;
  }
}
```

---

## ⚙️ Settings System

### **Complete Implementation**

#### **Features**
- ✅ **Multi-Theme Support**: Light, Dark, Ocean, Forest, High Contrast
- ✅ **Sound Effects Toggle**: Enable/disable audio feedback
- ✅ **Animation Speed**: Global speed control (0.5x to 2x)
- ✅ **Accessibility**: Font size, reduced motion, screen reader support
- ✅ **Data Management**: Clear progress, export/import data
- ✅ **Language Selection**: English, Spanish, French (future)
- ✅ **Notification Preferences**: Email, push notifications

#### **Settings Component (`settings.component.ts`)**

```typescript
export class SettingsComponent implements OnInit {
  // Theme settings
  themes = [
    { 
      id: 'light', 
      name: 'Light Mode', 
      icon: 'sun-fill',
      primary: '#007bff',
      background: '#ffffff'
    },
    { 
      id: 'dark', 
      name: 'Dark Mode', 
      icon: 'moon-stars-fill',
      primary: '#4a90e2',
      background: '#1a1a2e'
    },
    { 
      id: 'ocean', 
      name: 'Ocean Blue', 
      icon: 'water',
      primary: '#0077b6',
      background: '#023e8a'
    },
    { 
      id: 'forest', 
      name: 'Forest Green', 
      icon: 'tree-fill',
      primary: '#2d6a4f',
      background: '#1b4332'
    },
    {
      id: 'high-contrast',
      name: 'High Contrast',
      icon: 'circle-half',
      primary: '#ffff00',
      background: '#000000'
    }
  ];
  
  // Current settings
  currentSettings = {
    theme: 'dark',
    soundEnabled: true,
    animationSpeed: 1,
    fontSize: 'medium',
    reducedMotion: false,
    language: 'en',
    notifications: {
      email: true,
      push: false
    }
  };
  
  constructor(
    private soundService: SoundService,
    private renderer: Renderer2,
    @Inject(DOCUMENT) private document: Document
  ) {}
  
  ngOnInit(): void {
    this.loadSettings();
    this.applySettings();
  }
  
  // Theme Management
  changeTheme(themeId: string): void {
    this.currentSettings.theme = themeId;
    
    // Remove all theme classes
    this.themes.forEach(theme => {
      this.renderer.removeClass(this.document.body, theme.id);
    });
    
    // Add new theme class
    this.renderer.addClass(this.document.body, themeId);
    
    // Update CSS variables
    const theme = this.themes.find(t => t.id === themeId);
    if (theme) {
      this.document.documentElement.style.setProperty('--primary-color', theme.primary);
      this.document.documentElement.style.setProperty('--background-color', theme.background);
    }
    
    this.saveSettings();
    this.playFeedbackSound();
  }
  
  // Sound Settings
  toggleSound(): void {
    this.currentSettings.soundEnabled = !this.currentSettings.soundEnabled;
    this.soundService.toggle();
    this.saveSettings();
    
    if (this.currentSettings.soundEnabled) {
      this.soundService.play('complete');
    }
  }
  
  // Animation Speed
  changeAnimationSpeed(speed: number): void {
    this.currentSettings.animationSpeed = speed;
    
    // Update GSAP global speed
    gsap.globalTimeline.timeScale(speed);
    
    this.saveSettings();
    this.playFeedbackSound();
  }
  
  // Font Size
  changeFontSize(size: 'small' | 'medium' | 'large' | 'xlarge'): void {
    this.currentSettings.fontSize = size;
    
    const fontSizes = {
      small: '14px',
      medium: '16px',
      large: '18px',
      xlarge: '20px'
    };
    
    this.document.documentElement.style.setProperty(
      '--base-font-size', 
      fontSizes[size]
    );
    
    this.saveSettings();
  }
  
  // Accessibility
  toggleReducedMotion(): void {
    this.currentSettings.reducedMotion = !this.currentSettings.reducedMotion;
    
    if (this.currentSettings.reducedMotion) {
      // Disable animations
      this.document.documentElement.style.setProperty(
        '--animation-duration', 
        '0s'
      );
      gsap.globalTimeline.timeScale(100); // Instant animations
    } else {
      // Enable animations
      this.document.documentElement.style.setProperty(
        '--animation-duration', 
        '0.3s'
      );
      gsap.globalTimeline.timeScale(this.currentSettings.animationSpeed);
    }
    
    this.saveSettings();
  }
  
  // Data Management
  clearAllProgress(): void {
    if (confirm('Are you sure you want to clear all progress? This cannot be undone.')) {
      const user = this.authService.getStoredUser();
      if (user) {
        const userIdentifier = user.email || user.displayName;
        localStorage.removeItem(`quiz_progress_${userIdentifier}`);
        localStorage.removeItem(`progress_${userIdentifier}`);
      }
      
      alert('All progress has been cleared.');
      this.playFeedbackSound();
    }
  }
  
  exportData(): void {
    const user = this.authService.getStoredUser();
    if (!user) return;
    
    const userIdentifier = user.email || user.displayName;
    const data = {
      user: user,
      quizProgress: localStorage.getItem(`quiz_progress_${userIdentifier}`),
      progress: localStorage.getItem(`progress_${userIdentifier}`),
      settings: this.currentSettings,
      exportDate: new Date().toISOString()
    };
    
    const blob = new Blob([JSON.stringify(data, null, 2)], { 
      type: 'application/json' 
    });
    const url = URL.createObjectURL(blob);
    const link = document.createElement('a');
    link.href = url;
    link.download = `algorithm-visualizer-data-${Date.now()}.json`;
    link.click();
    
    this.playFeedbackSound();
  }
  
  importData(event: any): void {
    const file = event.target.files[0];
    if (!file) return;
    
    const reader = new FileReader();
    reader.onload = (e: any) => {
      try {
        const data = JSON.parse(e.target.result);
        
        // Restore data
        const user = this.authService.getStoredUser();
        const userIdentifier = user?.email || user?.displayName;
        
        if (data.quizProgress) {
          localStorage.setItem(`quiz_progress_${userIdentifier}`, data.quizProgress);
        }
        if (data.progress) {
          localStorage.setItem(`progress_${userIdentifier}`, data.progress);
        }
        if (data.settings) {
          this.currentSettings = data.settings;
          this.applySettings();
        }
        
        alert('Data imported successfully!');
        this.playFeedbackSound();
      } catch (error) {
        alert('Error importing data. Please check the file format.');
      }
    };
    reader.readAsText(file);
  }
  
  // Notification Settings
  updateNotificationSettings(): void {
    this.saveSettings();
    this.playFeedbackSound();
  }
  
  // Persistence
  saveSettings(): void {
    localStorage.setItem('app-settings', JSON.stringify(this.currentSettings));
  }
  
  loadSettings(): void {
    const saved = localStorage.getItem('app-settings');
    if (saved) {
      try {
        this.currentSettings = { ...this.currentSettings, ...JSON.parse(saved) };
      } catch (error) {
        console.warn('Error loading settings:', error);
      }
    }
  }
  
  applySettings(): void {
    this.changeTheme(this.currentSettings.theme);
    this.changeFontSize(this.currentSettings.fontSize);
    this.changeAnimationSpeed(this.currentSettings.animationSpeed);
    
    if (this.currentSettings.reducedMotion) {
      this.toggleReducedMotion();
    }
    
    if (!this.currentSettings.soundEnabled) {
      this.soundService.toggle();
    }
  }
  
  playFeedbackSound(): void {
    if (this.currentSettings.soundEnabled) {
      this.soundService.play('complete');
    }
  }
  
  // Reset to defaults
  resetToDefaults(): void {
    if (confirm('Reset all settings to default values?')) {
      this.currentSettings = {
        theme: 'dark',
        soundEnabled: true,
        animationSpeed: 1,
        fontSize: 'medium',
        reducedMotion: false,
        language: 'en',
        notifications: {
          email: true,
          push: false
        }
      };
      
      this.applySettings();
      this.saveSettings();
      alert('Settings reset to defaults');
    }
  }
}
```

#### **Settings Template (`settings.component.html`)**

```html
<div class="settings-container">
  <h2 class="settings-title">
    <i class="bi bi-gear-fill"></i> Settings
  </h2>
  
  <!-- Theme Selection -->
  <div class="settings-section">
    <h3>Appearance</h3>
    
    <div class="theme-grid">
      <div *ngFor="let theme of themes" 
           class="theme-card"
           [class.active]="currentSettings.theme === theme.id"
           (click)="changeTheme(theme.id)">
        <i class="bi bi-{{theme.icon}}"></i>
        <span>{{theme.name}}</span>
        <div class="color-preview" [style.background]="theme.primary"></div>
      </div>
    </div>
    
    <div class="setting-item">
      <label>Font Size</label>
      <select [(ngModel)]="currentSettings.fontSize" 
              (change)="changeFontSize(currentSettings.fontSize)">
        <option value="small">Small</option>
        <option value="medium">Medium</option>
        <option value="large">Large</option>
        <option value="xlarge">Extra Large</option>
      </select>
    </div>
  </div>
  
  <!-- Animation Settings -->
  <div class="settings-section">
    <h3>Animations</h3>
    
    <div class="setting-item">
      <label>Animation Speed: {{currentSettings.animationSpeed}}x</label>
      <input type="range" 
             min="0.5" 
             max="2" 
             step="0.25"
             [(ngModel)]="currentSettings.animationSpeed"
             (input)="changeAnimationSpeed(currentSettings.animationSpeed)" />
    </div>
    
    <div class="setting-item">
      <label>
        <input type="checkbox" 
               [(ngModel)]="currentSettings.reducedMotion"
               (change)="toggleReducedMotion()" />
        Reduce Motion (Accessibility)
      </label>
    </div>
  </div>
  
  <!-- Sound Settings -->
  <div class="settings-section">
    <h3>Sound</h3>
    
    <div class="setting-item">
      <label>
        <input type="checkbox" 
               [(ngModel)]="currentSettings.soundEnabled"
               (change)="toggleSound()" />
        Enable Sound Effects
      </label>
    </div>
  </div>
  
  <!-- Data Management -->
  <div class="settings-section">
    <h3>Data Management</h3>
    
    <button class="btn btn-primary" (click)="exportData()">
      <i class="bi bi-download"></i> Export Progress
    </button>
    
    <button class="btn btn-secondary" (click)="fileInput.click()">
      <i class="bi bi-upload"></i> Import Progress
    </button>
    <input #fileInput type="file" accept=".json" 
           (change)="importData($event)" style="display: none" />
    
    <button class="btn btn-danger" (click)="clearAllProgress()">
      <i class="bi bi-trash"></i> Clear All Progress
    </button>
  </div>
  
  <!-- Notifications -->
  <div class="settings-section">
    <h3>Notifications</h3>
    
    <div class="setting-item">
      <label>
        <input type="checkbox" 
               [(ngModel)]="currentSettings.notifications.email"
               (change)="updateNotificationSettings()" />
        Email Notifications
      </label>
    </div>
    
    <div class="setting-item">
      <label>
        <input type="checkbox" 
               [(ngModel)]="currentSettings.notifications.push"
               (change)="updateNotificationSettings()" />
        Push Notifications
      </label>
    </div>
  </div>
  
  <!-- Reset -->
  <div class="settings-section">
    <button class="btn btn-outline-secondary" (click)="resetToDefaults()">
      <i class="bi bi-arrow-counterclockwise"></i> Reset to Defaults
    </button>
  </div>
</div>
```

---

## ❓ FAQ System

### **Complete Implementation**

#### **Features**
- ✅ **Categorized Questions**: Getting Started, Account, Progress, Quizzes, Code Editor, Technical
- ✅ **Search Functionality**: Real-time filtering
- ✅ **Expandable Answers**: Accordion-style UI
- ✅ **Rich Content**: Code snippets, links, images
- ✅ **Contact Support**: Quick access to help

#### **FAQs Component (`faqs.component.ts`)**

```typescript
export class FaqsComponent implements OnInit {
  searchTerm = '';
  expandedQuestions: Set<string> = new Set();
  
  faqCategories = [
    {
      name: 'Getting Started',
      icon: 'rocket-takeoff',
      color: '#4a90e2',
      questions: [
        {
          id: 'gs-1',
          question: 'How do I visualize an algorithm?',
          answer: `To visualize an algorithm:
1. Navigate to DSA or DAA section from the navbar
2. Select an algorithm (e.g., Bubble Sort, Binary Search Tree)
3. Click the Play button to start visualization
4. Use controls: Play, Pause, Step Forward/Backward
5. Adjust speed using the speed slider (0.5x to 2x)`,
          tags: ['visualization', 'beginner']
        },
        {
          id: 'gs-2',
          question: 'What are the control buttons?',
          answer: `Control buttons explained:
• ▶️ Play - Start automatic visualization
• ⏸️ Pause - Pause at current step
• ⏹️ Stop - Reset to beginning
• ⏭️ Next - Step forward one action
• ⏮️ Previous - Step backward one action
• 🔊 Sound - Toggle audio feedback`,
          tags: ['controls', 'beginner']
        },
        {
          id: 'gs-3',
          question: 'How do I take a quiz?',
          answer: `After completing an algorithm visualization:
1. Click "Take Quiz" button
2. Answer multiple-choice questions
3. Get instant feedback on each answer
4. View explanation for correct/wrong answers
5. Finish to see your score (Green: 100%, Yellow: 60-99%, Review: <60%)
6. Retry if score < 100% to unlock full completion`,
          tags: ['quiz', 'beginner']
        }
      ]
    },
    {
      name: 'Account & Progress',
      icon: 'person-circle',
      color: '#7c3aed',
      questions: [
        {
          id: 'ap-1',
          question: 'Do I need to register?',
          answer: `No, registration is optional! You can:
• **Guest Mode**: Continue without account, progress saved locally
• **Registered User**: Sign up to save progress to cloud and access from any device

Guest progress is stored in browser localStorage and will be lost if you clear browser data.`,
          tags: ['registration', 'account']
        },
        {
          id: 'ap-2',
          question: 'How is my progress tracked?',
          answer: `Progress tracking system:
• **Local Storage**: Immediate saving for offline access
• **Cloud Sync**: Automatic backend synchronization for registered users
• **Real-Time Updates**: Navbar shows completion status instantly
• **Color Coding**: 
  - 🟢 Green = 100% quiz score (fully completed)
  - 🟡 Yellow = 60-99% score (needs retry)
  - 🔴 Red = <60% score (review needed)`,
          tags: ['progress', 'tracking']
        },
        {
          id: 'ap-3',
          question: 'Can I export my progress?',
          answer: `Yes! Go to Settings → Data Management:
• **Export**: Download JSON file with all progress
• **Import**: Upload JSON file to restore progress
• **Clear**: Reset all progress (cannot be undone)

Exported data includes: quiz scores, algorithm completions, settings, badges earned.`,
          tags: ['export', 'data']
        }
      ]
    },
    {
      name: 'Quizzes',
      icon: 'question-circle',
      color: '#ff6b35',
      questions: [
        {
          id: 'q-1',
          question: 'How does quiz scoring work?',
          answer: `Quiz scoring system:
• Each question is worth equal points
• Score = (Correct Answers / Total Questions) × 100
• **Green Status (100%)**: Perfect score, algorithm fully completed
• **Yellow Status (60-99%)**: Good job, but needs retry for full completion
• **Red Status (<60%)**: Review the algorithm and try again

Tip: You must score 100% to unlock full completion and move to next algorithm!`,
          tags: ['quiz', 'scoring']
        },
        {
          id: 'q-2',
          question: 'Can I retake quizzes?',
          answer: `Yes! Retry mechanism:
• Click "Retry Quiz" after seeing results
• Questions are shuffled each time
• Previous score is overwritten by latest attempt
• No limit on retry attempts
• Only 100% score marks algorithm as fully completed`,
          tags: ['quiz', 'retry']
        },
        {
          id: 'q-3',
          question: 'What happens if I fail a quiz?',
          answer: `If you score <100%:
• Algorithm status shows Yellow (60-99%) or Red (<60%)
• You can still progress to other algorithms
• Review the visualization again for better understanding
• Retry the quiz for full completion
• Leaderboard counts all scores, not just perfect ones`,
          tags: ['quiz', 'failure']
        }
      ]
    },
    {
      name: 'Code Editor',
      icon: 'code-slash',
      color: '#51cf66',
      questions: [
        {
          id: 'ce-1',
          question: 'Which programming languages are supported?',
          answer: `Monaco Editor (VS Code engine) supports:
• **JavaScript** - Full syntax highlighting and IntelliSense
• **Python** - Python 3.x syntax
• **Java** - Java 11+ support
• **C++** - Modern C++ standards

More languages coming soon: Go, Rust, TypeScript, Swift!`,
          tags: ['code-editor', 'languages']
        },
        {
          id: 'ce-2',
          question: 'Can I run my code?',
          answer: `Code execution:
• Currently in **mock mode** (returns sample output)
• Real execution planned using Docker containers
• Will support test cases and validation
• Time and memory limits will be enforced

For now, use the editor for learning and practicing syntax!`,
          tags: ['code-editor', 'execution']
        },
        {
          id: 'ce-3',
          question: 'How do I save my code?',
          answer: `Code saving:
• Code is auto-saved to browser localStorage
• Use Ctrl+S (Cmd+S on Mac) to manually save
• Export code as .txt/.java/.py/.cpp file
• Reset button clears to default template
• Copy to clipboard button for quick sharing`,
          tags: ['code-editor', 'saving']
        }
      ]
    },
    {
      name: 'Technical',
      icon: 'gear-fill',
      color: '#ff6b6b',
      questions: [
        {
          id: 't-1',
          question: 'What technologies power this app?',
          answer: `Tech stack:
**Frontend:**
• Angular 20 + TypeScript
• D3.js (visualizations)
• GSAP (animations)
• Monaco Editor (code editor)
• Bootstrap 5

**Backend:**
• Spring Boot 3.4.0
• Java 21
• H2 Database (development)
• JWT Authentication
• Spring Security`,
          tags: ['technical', 'stack']
        },
        {
          id: 't-2',
          question: 'Is my data secure?',
          answer: `Security measures:
• JWT tokens with 7-day expiration
• HTTPS encryption (production)
• Spring Security with CORS protection
• Password hashing (BCrypt planned)
• No sensitive data stored in localStorage
• Regular security audits

Guest users: Data stored locally, never sent to server.`,
          tags: ['security', 'privacy']
        },
        {
          id: 't-3',
          question: 'Why are animations slow/fast?',
          answer: `Animation speed control:
• Go to Settings → Animations
• Adjust speed slider (0.5x to 2x)
• Enable "Reduce Motion" for instant animations (accessibility)
• Clear browser cache if animations stutter
• Disable other browser tabs for better performance

Recommended: 1x speed for learning, 1.5x for review.`,
          tags: ['performance', 'animations']
        },
        {
          id: 't-4',
          question: 'Can I use this offline?',
          answer: `Offline support:
• **Current**: Guest mode works offline, progress saved locally
• **Planned**: Progressive Web App (PWA) for full offline access
• **Future**: Service workers for caching

For now: Register and use online for best experience!`,
          tags: ['offline', 'pwa']
        }
      ]
    },
    {
      name: 'Troubleshooting',
      icon: 'tools',
      color: '#f59e0b',
      questions: [
        {
          id: 'ts-1',
          question: 'Visualization not showing?',
          answer: `Try these fixes:
1. Hard refresh: Ctrl+Shift+R (Cmd+Shift+R on Mac)
2. Clear browser cache and cookies
3. Disable browser extensions (especially ad blockers)
4. Try a different browser (Chrome/Firefox recommended)
5. Check browser console for errors (F12)

Still not working? Contact support with error details.`,
          tags: ['troubleshooting', 'bug']
        },
        {
          id: 'ts-2',
          question: 'Lost my progress?',
          answer: `Recovery steps:
• **Registered Users**: Progress synced to cloud, just login again
• **Guest Users**: Check if localStorage was cleared
• Go to Settings → Import Progress (if you exported before)
• Browser incognito mode doesn't save progress

Prevention: Register account or export progress regularly!`,
          tags: ['troubleshooting', 'progress']
        },
        {
          id: 'ts-3',
          question: 'Quiz not loading?',
          answer: `Solutions:
1. Refresh the page
2. Check internet connection (quizzes load from server)
3. Try completing visualization first
4. Clear browser cache
5. Check if backend server is running (dev mode)

Error message? Note it and contact support.`,
          tags: ['troubleshooting', 'quiz']
        }
      ]
    }
  ];
  
  ngOnInit(): void {
    // Load from URL if question ID present
    const urlParams = new URLSearchParams(window.location.search);
    const questionId = urlParams.get('q');
    if (questionId) {
      this.expandQuestion(questionId);
      this.scrollToQuestion(questionId);
    }
  }
  
  // Filter FAQs by search term
  getFilteredFaqs() {
    if (!this.searchTerm) return this.faqCategories;
    
    const term = this.searchTerm.toLowerCase();
    
    return this.faqCategories.map(category => ({
      ...category,
      questions: category.questions.filter(q =>
        q.question.toLowerCase().includes(term) ||
        q.answer.toLowerCase().includes(term) ||
        q.tags.some(tag => tag.includes(term))
      )
    })).filter(category => category.questions.length > 0);
  }
  
  // Toggle question expansion
  toggleQuestion(questionId: string): void {
    if (this.expandedQuestions.has(questionId)) {
      this.expandedQuestions.delete(questionId);
    } else {
      this.expandedQuestions.add(questionId);
    }
  }
  
  expandQuestion(questionId: string): void {
    this.expandedQuestions.add(questionId);
  }
  
  isExpanded(questionId: string): boolean {
    return this.expandedQuestions.has(questionId);
  }
  
  scrollToQuestion(questionId: string): void {
    setTimeout(() => {
      const element = document.getElementById(questionId);
      if (element) {
        element.scrollIntoView({ behavior: 'smooth', block: 'center' });
      }
    }, 300);
  }
  
  // Share question link
  shareQuestion(questionId: string): void {
    const url = `${window.location.origin}/faqs?q=${questionId}`;
    navigator.clipboard.writeText(url).then(() => {
      alert('Link copied to clipboard!');
    });
  }
  
  // Clear search
  clearSearch(): void {
    this.searchTerm = '';
  }
  
  // Expand all
  expandAll(): void {
    this.getFilteredFaqs().forEach(category => {
      category.questions.forEach(q => {
        this.expandedQuestions.add(q.id);
      });
    });
  }
  
  // Collapse all
  collapseAll(): void {
    this.expandedQuestions.clear();
  }
}
```

#### **FAQs Template (`faqs.component.html`)**

```html
<div class="faqs-container">
  <div class="faqs-header">
    <h1><i class="bi bi-question-circle-fill"></i> Frequently Asked Questions</h1>
    <p>Find answers to common questions about Algorithm Visualizer</p>
  </div>
  
  <!-- Search Bar -->
  <div class="search-section">
    <div class="search-box">
      <i class="bi bi-search"></i>
      <input type="text" 
             [(ngModel)]="searchTerm" 
             placeholder="Search FAQs..."
             class="search-input" />
      <button *ngIf="searchTerm" (click)="clearSearch()" class="clear-btn">
        <i class="bi bi-x-circle-fill"></i>
      </button>
    </div>
    
    <div class="search-actions">
      <button class="btn-link" (click)="expandAll()">Expand All</button>
      <button class="btn-link" (click)="collapseAll()">Collapse All</button>
    </div>
  </div>
  
  <!-- FAQ Categories -->
  <div class="faq-categories">
    <div *ngFor="let category of getFilteredFaqs()" class="faq-category">
      <div class="category-header" [style.border-left-color]="category.color">
        <i class="bi bi-{{category.icon}}" [style.color]="category.color"></i>
        <h2>{{category.name}}</h2>
        <span class="question-count">{{category.questions.length}} questions</span>
      </div>
      
      <div class="questions-list">
        <div *ngFor="let question of category.questions" 
             [id]="question.id"
             class="question-item"
             [class.expanded]="isExpanded(question.id)">
          
          <div class="question-header" (click)="toggleQuestion(question.id)">
            <i class="bi bi-chevron-right toggle-icon"></i>
            <span class="question-text">{{question.question}}</span>
            <button class="share-btn" 
                    (click)="$event.stopPropagation(); shareQuestion(question.id)"
                    title="Share this question">
              <i class="bi bi-share"></i>
            </button>
          </div>
          
          <div class="answer-content" *ngIf="isExpanded(question.id)">
            <div [innerHTML]="question.answer | markdown"></div>
            
            <div class="question-tags">
              <span *ngFor="let tag of question.tags" class="tag">
                #{{tag}}
              </span>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
  
  <!-- No Results -->
  <div *ngIf="getFilteredFaqs().length === 0" class="no-results">
    <i class="bi bi-search"></i>
    <h3>No results found for "{{searchTerm}}"</h3>
    <p>Try different keywords or <button (click)="clearSearch()">clear search</button></p>
  </div>
  
  <!-- Contact Support -->
  <div class="contact-section">
    <h3>Still have questions?</h3>
    <p>Can't find what you're looking for? We're here to help!</p>
    <a routerLink="/contact" class="btn btn-primary">
      <i class="bi bi-envelope-fill"></i> Contact Support
    </a>
  </div>
</div>
```

---

## � Complete Algorithm Catalog

### **DSA (Data Structures & Algorithms) - 7 Implementations**

#### **1. Stack Component**
**Path**: `frontend/src/app/pages/dsa/stack.component.ts`

**Features:**
- ✅ LIFO (Last In First Out) visualization
- ✅ Push/Pop operations with animations
- ✅ Peek functionality
- ✅ Stack overflow detection
- ✅ Visual representation with stacked boxes

**Key Operations:**
```typescript
push(value: number): void {
  if (this.stack.length >= this.maxSize) {
    alert('Stack Overflow!');
    return;
  }
  this.stack.push(value);
  this.animatePush();
}

pop(): number | undefined {
  if (this.isEmpty()) {
    alert('Stack Underflow!');
    return undefined;
  }
  const value = this.stack.pop();
  this.animatePop();
  return value;
}
```

**Time Complexity:**
- Push: O(1)
- Pop: O(1)
- Peek: O(1)

---

#### **2. Queue Component**
**Path**: `frontend/src/app/pages/dsa/queue.component.ts`

**Features:**
- ✅ FIFO (First In First Out) visualization
- ✅ Enqueue/Dequeue operations
- ✅ Front and Rear pointers
- ✅ Circular queue option
- ✅ Queue full/empty detection

**Key Operations:**
```typescript
enqueue(value: number): void {
  if (this.isFull()) {
    alert('Queue is full!');
    return;
  }
  this.queue.push(value);
  this.updateVisualization();
}

dequeue(): number | undefined {
  if (this.isEmpty()) {
    alert('Queue is empty!');
    return undefined;
  }
  const value = this.queue.shift();
  this.updateVisualization();
  return value;
}
```

**Time Complexity:**
- Enqueue: O(1)
- Dequeue: O(1)
- Peek: O(1)

---

#### **3. Linked List Component** ⭐ (Fully Documented Above)
**Path**: `frontend/src/app/pages/dsa/linked-list.component.ts` (1859 lines)

**Four Types:**
1. **Singly Linked List** - One-way traversal
2. **Circular Linked List** - Last node points to first (8-node limit)
3. **Doubly Linked List** - Two-way traversal with prev/next
4. **Doubly Circular** - Combined circular + doubly (purple theme)

---

#### **4. Hash Table Component**
**Path**: `frontend/src/app/pages/dsa/hash-table.component.ts`

**Features:**
- ✅ Hash function visualization
- ✅ Collision resolution (chaining)
- ✅ Insert/Search/Delete operations
- ✅ Load factor calculation
- ✅ Rehashing demonstration

**Key Operations:**
```typescript
hashFunction(key: string): number {
  let hash = 0;
  for (let i = 0; i < key.length; i++) {
    hash = (hash * 31 + key.charCodeAt(i)) % this.tableSize;
  }
  return hash;
}

insert(key: string, value: any): void {
  const index = this.hashFunction(key);
  
  if (!this.table[index]) {
    this.table[index] = [];
  }
  
  // Chaining for collision resolution
  this.table[index].push({ key, value });
  this.visualizeInsertion(index);
}
```

**Time Complexity:**
- Average Insert: O(1)
- Average Search: O(1)
- Worst Case: O(n) with collisions

---

#### **5. Priority Queue Component**
**Path**: `frontend/src/app/pages/dsa/priority-queue.component.ts`

**Features:**
- ✅ Min-heap and Max-heap modes
- ✅ Priority-based insertion
- ✅ Extract min/max operations
- ✅ Heap property visualization
- ✅ Heapify animation

**Key Operations:**
```typescript
insert(value: number, priority: number): void {
  this.heap.push({ value, priority });
  this.heapifyUp(this.heap.length - 1);
  this.visualizeHeap();
}

extractMin(): any {
  if (this.heap.length === 0) return null;
  
  const min = this.heap[0];
  const last = this.heap.pop();
  
  if (this.heap.length > 0) {
    this.heap[0] = last!;
    this.heapifyDown(0);
  }
  
  return min;
}
```

**Time Complexity:**
- Insert: O(log n)
- Extract Min/Max: O(log n)
- Peek: O(1)

---

#### **6. Deque Component**
**Path**: `frontend/src/app/pages/dsa/deque.component.ts`

**Features:**
- ✅ Double-ended queue
- ✅ Insert/Remove from both ends
- ✅ Front and Rear operations
- ✅ Bidirectional visualization

**Key Operations:**
```typescript
insertFront(value: number): void {
  this.deque.unshift(value);
  this.animateInsertFront();
}

insertRear(value: number): void {
  this.deque.push(value);
  this.animateInsertRear();
}

deleteFront(): number | undefined {
  return this.deque.shift();
}

deleteRear(): number | undefined {
  return this.deque.pop();
}
```

**Time Complexity:**
- All operations: O(1)

---

### **DAA (Design & Analysis of Algorithms) - 14 Implementations**

#### **Sorting Algorithms (1 algorithm)**

##### **1. Quick Sort**
**Path**: `frontend/src/app/pages/daa/quick-sort.component.ts`

**Features:**
- ✅ Pivot selection strategies (first, last, median, random)
- ✅ Partition animation
- ✅ Recursive call visualization
- ✅ Comparison counter

**Algorithm:**
```typescript
quickSort(arr: number[], low: number, high: number): void {
  if (low < high) {
    const pi = this.partition(arr, low, high);
    
    this.steps.push({
      array: [...arr],
      pivot: pi,
      description: `Pivot ${arr[pi]} placed at correct position`
    });
    
    this.quickSort(arr, low, pi - 1);
    this.quickSort(arr, pi + 1, high);
  }
}

partition(arr: number[], low: number, high: number): number {
  const pivot = arr[high];
  let i = low - 1;
  
  for (let j = low; j < high; j++) {
    if (arr[j] < pivot) {
      i++;
      [arr[i], arr[j]] = [arr[j], arr[i]];
    }
  }
  
  [arr[i + 1], arr[high]] = [arr[high], arr[i + 1]];
  return i + 1;
}
```

**Complexity:**
- Best/Average: O(n log n)
- Worst: O(n²)
- Space: O(log n)

---

#### **Dynamic Programming (5 algorithms)**

##### **2. Fibonacci Sequence**
**Path**: `frontend/src/app/pages/daa/fibonacci.component.ts`

**Three Approaches:**
1. **Recursive** - Exponential time O(2^n)
2. **Memoization** - Top-down DP O(n)
3. **Tabulation** - Bottom-up DP O(n)

**Code:**
```typescript
// Memoization approach
fibonacciMemo(n: number, memo: Map<number, number> = new Map()): number {
  if (n <= 1) return n;
  
  if (memo.has(n)) {
    return memo.get(n)!;
  }
  
  const result = this.fibonacciMemo(n - 1, memo) + this.fibonacciMemo(n - 2, memo);
  memo.set(n, result);
  
  return result;
}

// Tabulation approach
fibonacciTab(n: number): number {
  const dp: number[] = new Array(n + 1);
  dp[0] = 0;
  dp[1] = 1;
  
  for (let i = 2; i <= n; i++) {
    dp[i] = dp[i - 1] + dp[i - 2];
    this.visualizeStep(i, dp);
  }
  
  return dp[n];
}
```

---

##### **3. Longest Common Subsequence (LCS)**
**Path**: `frontend/src/app/pages/daa/lcs.component.ts`

**Features:**
- ✅ DP table visualization
- ✅ Subsequence highlighting
- ✅ Backtracking animation

**Algorithm:**
```typescript
findLCS(str1: string, str2: string): string {
  const m = str1.length;
  const n = str2.length;
  const dp: number[][] = Array(m + 1).fill(0).map(() => Array(n + 1).fill(0));
  
  // Fill DP table
  for (let i = 1; i <= m; i++) {
    for (let j = 1; j <= n; j++) {
      if (str1[i - 1] === str2[j - 1]) {
        dp[i][j] = dp[i - 1][j - 1] + 1;
      } else {
        dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]);
      }
      
      this.visualizeDPTable(dp, i, j);
    }
  }
  
  // Backtrack to find LCS
  return this.backtrack(dp, str1, str2, m, n);
}
```

**Complexity:**
- Time: O(m × n)
- Space: O(m × n)

---

##### **4. Edit Distance (Levenshtein Distance)**
**Path**: `frontend/src/app/pages/daa/edit-distance.component.ts`

**Operations:**
- Insert, Delete, Replace

**Algorithm:**
```typescript
editDistance(str1: string, str2: string): number {
  const m = str1.length;
  const n = str2.length;
  const dp: number[][] = Array(m + 1).fill(0).map(() => Array(n + 1).fill(0));
  
  // Initialize base cases
  for (let i = 0; i <= m; i++) dp[i][0] = i;
  for (let j = 0; j <= n; j++) dp[0][j] = j;
  
  // Fill DP table
  for (let i = 1; i <= m; i++) {
    for (let j = 1; j <= n; j++) {
      if (str1[i - 1] === str2[j - 1]) {
        dp[i][j] = dp[i - 1][j - 1];
      } else {
        dp[i][j] = 1 + Math.min(
          dp[i - 1][j],     // Delete
          dp[i][j - 1],     // Insert
          dp[i - 1][j - 1]  // Replace
        );
      }
    }
  }
  
  return dp[m][n];
}
```

**Complexity:**
- Time: O(m × n)
- Space: O(m × n)

---

##### **5. Coin Change Problem**
**Path**: `frontend/src/app/pages/daa/coin-change.component.ts`

**Two Variants:**
1. **Minimum Coins** - Fewest coins to make amount
2. **Number of Ways** - Count combinations

**Algorithm:**
```typescript
minCoins(coins: number[], amount: number): number {
  const dp: number[] = new Array(amount + 1).fill(Infinity);
  dp[0] = 0;
  
  for (let i = 1; i <= amount; i++) {
    for (const coin of coins) {
      if (coin <= i) {
        dp[i] = Math.min(dp[i], dp[i - coin] + 1);
      }
    }
    this.visualizeDP(dp, i);
  }
  
  return dp[amount] === Infinity ? -1 : dp[amount];
}
```

**Complexity:**
- Time: O(amount × coins)
- Space: O(amount)

---

#### **Greedy Algorithms (2 algorithms)**

##### **6. Fractional Knapsack**
**Path**: `frontend/src/app/pages/daa/fractional-knapsack.component.ts`

**Features:**
- ✅ Value-to-weight ratio calculation
- ✅ Sorting by ratio
- ✅ Greedy selection visualization

**Algorithm:**
```typescript
fractionalKnapsack(capacity: number, items: Item[]): number {
  // Calculate value/weight ratio
  items.forEach(item => {
    item.ratio = item.value / item.weight;
  });
  
  // Sort by ratio (descending)
  items.sort((a, b) => b.ratio - a.ratio);
  
  let totalValue = 0;
  let remainingCapacity = capacity;
  
  for (const item of items) {
    if (remainingCapacity >= item.weight) {
      // Take whole item
      totalValue += item.value;
      remainingCapacity -= item.weight;
      this.visualizeSelection(item, 1.0);
    } else {
      // Take fraction
      const fraction = remainingCapacity / item.weight;
      totalValue += item.value * fraction;
      this.visualizeSelection(item, fraction);
      break;
    }
  }
  
  return totalValue;
}
```

**Complexity:**
- Time: O(n log n)
- Space: O(1)

---

##### **7. Huffman Coding**
**Path**: `frontend/src/app/pages/daa/huffman-coding.component.ts`

**Features:**
- ✅ Frequency table generation
- ✅ Binary tree construction
- ✅ Huffman code assignment
- ✅ Compression ratio display

**Algorithm:**
```typescript
buildHuffmanTree(text: string): HuffmanNode {
  // Calculate frequencies
  const freqMap = new Map<string, number>();
  for (const char of text) {
    freqMap.set(char, (freqMap.get(char) || 0) + 1);
  }
  
  // Create priority queue
  const pq: HuffmanNode[] = [];
  freqMap.forEach((freq, char) => {
    pq.push({ char, freq, left: null, right: null });
  });
  
  // Build tree
  while (pq.length > 1) {
    pq.sort((a, b) => a.freq - b.freq);
    
    const left = pq.shift()!;
    const right = pq.shift()!;
    
    const parent: HuffmanNode = {
      char: '',
      freq: left.freq + right.freq,
      left,
      right
    };
    
    pq.push(parent);
    this.visualizeTreeConstruction(parent);
  }
  
  return pq[0];
}
```

**Complexity:**
- Time: O(n log n)
- Space: O(n)

---

#### **Graph Algorithms (5 algorithms)**

##### **8. Dijkstra's Algorithm** ⭐
**Path**: `frontend/src/app/pages/daa/dijkstra.component.ts`

**Features:**
- ✅ Shortest path from single source
- ✅ Priority queue with min-heap
- ✅ Node distance updates
- ✅ Path highlighting

**Complete Implementation:**
```typescript
dijkstra(graph: Graph, startNode: number): Map<number, number> {
  const distances = new Map<number, number>();
  const visited = new Set<number>();
  const pq: [number, number][] = []; // [node, distance]
  
  // Initialize distances
  graph.nodes.forEach(node => {
    distances.set(node.id, node.id === startNode ? 0 : Infinity);
  });
  
  pq.push([startNode, 0]);
  
  while (pq.length > 0) {
    // Extract min
    pq.sort((a, b) => a[1] - b[1]);
    const [currentNode, currentDist] = pq.shift()!;
    
    if (visited.has(currentNode)) continue;
    visited.add(currentNode);
    
    this.visualizeNodeVisit(currentNode);
    
    // Update neighbors
    const neighbors = graph.edges.filter(e => e.source === currentNode);
    
    for (const edge of neighbors) {
      const newDist = currentDist + edge.weight;
      
      if (newDist < distances.get(edge.target)!) {
        distances.set(edge.target, newDist);
        pq.push([edge.target, newDist]);
        
        this.visualizeDistanceUpdate(edge.target, newDist);
      }
    }
  }
  
  return distances;
}
```

**Complexity:**
- Time: O((V + E) log V) with binary heap
- Space: O(V)

---

##### **9. Bellman-Ford Algorithm**
**Path**: `frontend/src/app/pages/daa/bellman-ford.component.ts`

**Features:**
- ✅ Handles negative weights
- ✅ Negative cycle detection
- ✅ Edge relaxation visualization

**Algorithm:**
```typescript
bellmanFord(graph: Graph, startNode: number): Map<number, number> | null {
  const distances = new Map<number, number>();
  
  // Initialize
  graph.nodes.forEach(node => {
    distances.set(node.id, node.id === startNode ? 0 : Infinity);
  });
  
  // Relax edges V-1 times
  for (let i = 0; i < graph.nodes.length - 1; i++) {
    for (const edge of graph.edges) {
      const dist = distances.get(edge.source)!;
      if (dist !== Infinity) {
        const newDist = dist + edge.weight;
        if (newDist < distances.get(edge.target)!) {
          distances.set(edge.target, newDist);
          this.visualizeRelaxation(edge, newDist);
        }
      }
    }
  }
  
  // Check for negative cycles
  for (const edge of graph.edges) {
    const dist = distances.get(edge.source)!;
    if (dist !== Infinity && dist + edge.weight < distances.get(edge.target)!) {
      alert('Negative cycle detected!');
      return null;
    }
  }
  
  return distances;
}
```

**Complexity:**
- Time: O(V × E)
- Space: O(V)

---

##### **10. Floyd-Warshall Algorithm**
**Path**: `frontend/src/app/pages/daa/floyd-warshall.component.ts`

**Features:**
- ✅ All-pairs shortest paths
- ✅ DP approach
- ✅ Distance matrix visualization

**Algorithm:**
```typescript
floydWarshall(graph: Graph): number[][] {
  const n = graph.nodes.length;
  const dist: number[][] = Array(n).fill(0).map(() => Array(n).fill(Infinity));
  
  // Initialize with direct edges
  for (let i = 0; i < n; i++) {
    dist[i][i] = 0;
  }
  
  for (const edge of graph.edges) {
    dist[edge.source][edge.target] = edge.weight;
  }
  
  // Floyd-Warshall DP
  for (let k = 0; k < n; k++) {
    for (let i = 0; i < n; i++) {
      for (let j = 0; j < n; j++) {
        if (dist[i][k] + dist[k][j] < dist[i][j]) {
          dist[i][j] = dist[i][k] + dist[k][j];
          this.visualizeUpdate(i, j, k, dist[i][j]);
        }
      }
    }
  }
  
  return dist;
}
```

**Complexity:**
- Time: O(V³)
- Space: O(V²)

---

##### **11. Prim's Algorithm**
**Path**: `frontend/src/app/pages/daa/prim.component.ts`

**Features:**
- ✅ Minimum Spanning Tree (MST)
- ✅ Greedy approach
- ✅ Tree edge highlighting

**Algorithm:**
```typescript
primMST(graph: Graph, startNode: number): Edge[] {
  const mstEdges: Edge[] = [];
  const visited = new Set<number>([startNode]);
  const pq: Edge[] = [];
  
  // Add all edges from start node
  graph.edges
    .filter(e => e.source === startNode)
    .forEach(e => pq.push(e));
  
  while (visited.size < graph.nodes.length && pq.length > 0) {
    // Sort by weight
    pq.sort((a, b) => a.weight - b.weight);
    const edge = pq.shift()!;
    
    if (visited.has(edge.target)) continue;
    
    // Add edge to MST
    mstEdges.push(edge);
    visited.add(edge.target);
    
    this.visualizeMSTEdge(edge);
    
    // Add new edges
    graph.edges
      .filter(e => e.source === edge.target && !visited.has(e.target))
      .forEach(e => pq.push(e));
  }
  
  return mstEdges;
}
```

**Complexity:**
- Time: O(E log V)
- Space: O(V)

---

##### **12. Kruskal's Algorithm**
**Path**: `frontend/src/app/pages/daa/kruskal.component.ts`

**Features:**
- ✅ Minimum Spanning Tree (MST)
- ✅ Union-Find data structure
- ✅ Edge sorting by weight

**Algorithm:**
```typescript
kruskalMST(graph: Graph): Edge[] {
  const mstEdges: Edge[] = [];
  const parent = new Map<number, number>();
  
  // Initialize Union-Find
  graph.nodes.forEach(node => parent.set(node.id, node.id));
  
  // Sort edges by weight
  const sortedEdges = [...graph.edges].sort((a, b) => a.weight - b.weight);
  
  for (const edge of sortedEdges) {
    const rootU = this.find(parent, edge.source);
    const rootV = this.find(parent, edge.target);
    
    if (rootU !== rootV) {
      mstEdges.push(edge);
      this.union(parent, rootU, rootV);
      this.visualizeMSTEdge(edge);
      
      if (mstEdges.length === graph.nodes.length - 1) break;
    }
  }
  
  return mstEdges;
}

find(parent: Map<number, number>, node: number): number {
  if (parent.get(node) !== node) {
    parent.set(node, this.find(parent, parent.get(node)!));
  }
  return parent.get(node)!;
}

union(parent: Map<number, number>, u: number, v: number): void {
  parent.set(u, v);
}
```

**Complexity:**
- Time: O(E log E)
- Space: O(V)

---

#### **Divide & Conquer (1 algorithm)**

##### **13. Strassen's Matrix Multiplication**
**Path**: `frontend/src/app/pages/daa/strassen.component.ts`

**Features:**
- ✅ Faster than standard O(n³) multiplication
- ✅ Recursive divide-and-conquer
- ✅ 7 multiplications instead of 8

**Algorithm:**
```typescript
strassenMultiply(A: number[][], B: number[][]): number[][] {
  const n = A.length;
  
  // Base case
  if (n === 1) {
    return [[A[0][0] * B[0][0]]];
  }
  
  // Divide matrices into quadrants
  const mid = n / 2;
  const A11 = this.subMatrix(A, 0, 0, mid);
  const A12 = this.subMatrix(A, 0, mid, mid);
  const A21 = this.subMatrix(A, mid, 0, mid);
  const A22 = this.subMatrix(A, mid, mid, mid);
  
  const B11 = this.subMatrix(B, 0, 0, mid);
  const B12 = this.subMatrix(B, 0, mid, mid);
  const B21 = this.subMatrix(B, mid, 0, mid);
  const B22 = this.subMatrix(B, mid, mid, mid);
  
  // Calculate 7 products (Strassen's formulas)
  const M1 = this.strassenMultiply(
    this.add(A11, A22), 
    this.add(B11, B22)
  );
  const M2 = this.strassenMultiply(
    this.add(A21, A22), 
    B11
  );
  // ... M3 through M7
  
  // Combine results
  const C11 = this.add(this.subtract(this.add(M1, M4), M5), M7);
  const C12 = this.add(M3, M5);
  const C21 = this.add(M2, M4);
  const C22 = this.add(this.subtract(this.add(M1, M3), M2), M6);
  
  return this.combine(C11, C12, C21, C22);
}
```

**Complexity:**
- Time: O(n^2.807) vs O(n³) standard
- Space: O(n²)

---

### **Additional Visualizations (Sorting - in visualize component)**

The main visualize component includes comprehensive implementations of:

1. **Bubble Sort** - O(n²), stable, comparison-based
2. **Selection Sort** - O(n²), unstable, in-place
3. **Insertion Sort** - O(n²), stable, adaptive
4. **Merge Sort** - O(n log n), stable, divide-and-conquer
5. **Heap Sort** - O(n log n), unstable, in-place
6. **Shell Sort** - O(n^1.5), unstable, gap-based
7. **Counting Sort** - O(n + k), stable, integer-only
8. **Radix Sort** - O(d × n), stable, digit-based

**Plus Searching Algorithms:**
- **Linear Search** - O(n)
- **Binary Search** - O(log n)
- **Jump Search** - O(√n)
- **Interpolation Search** - O(log log n) average

---

### **Summary of Algorithms**

| Category | Count | Algorithms |
|----------|-------|------------|
| **DSA - Data Structures** | 6 | Stack, Queue, Linked List (4 types), Hash Table, Priority Queue, Deque |
| **DAA - Sorting** | 1 | Quick Sort |
| **DAA - Dynamic Programming** | 4 | Fibonacci, LCS, Edit Distance, Coin Change |
| **DAA - Greedy** | 2 | Fractional Knapsack, Huffman Coding |
| **DAA - Graph** | 5 | Dijkstra, Bellman-Ford, Floyd-Warshall, Prim, Kruskal |
| **DAA - Divide & Conquer** | 1 | Strassen's Matrix |
| **Visualize - Sorting** | 8 | Bubble, Selection, Insertion, Merge, Heap, Shell, Counting, Radix |
| **Visualize - Searching** | 4 | Linear, Binary, Jump, Interpolation |
| **TOTAL** | **31 Algorithms** | Fully implemented with animations! |

---

## �🔬 Testing Strategy

### **Backend Testing**

**1. Unit Tests (JUnit)**
```java
@SpringBootTest
class UserServiceTest {
    
    @Autowired
    private UserRepository userRepository;
    
    @Test
    void testCreateUser() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setDisplayName("Test User");
        user.setPasswordHash("password123");
        
        User saved = userRepository.save(user);
        
        assertNotNull(saved.getId());
        assertEquals("test@example.com", saved.getEmail());
    }
    
    @Test
    void testFindUserByEmail() {
        Optional<User> user = userRepository.findByEmailIgnoreCase("test@example.com");
        assertTrue(user.isPresent());
    }
}
```

**2. Integration Tests**
```java
@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @Test
    void testLogin() throws Exception {
        mockMvc.perform(post("/api/auth/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"username\":\"test@example.com\",\"password\":\"password123\"}"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.accessToken").exists());
    }
}
```

### **Frontend Testing**

**1. Component Tests (Jasmine/Karma)**
```typescript
describe('LoginComponent', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;
  
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [LoginComponent, HttpClientTestingModule]
    }).compileComponents();
    
    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
  });
  
  it('should create', () => {
    expect(component).toBeTruthy();
  });
  
  it('should validate email format', () => {
    const emailControl = component.authForm.get('email');
    emailControl?.setValue('invalid-email');
    expect(emailControl?.invalid).toBeTruthy();
    
    emailControl?.setValue('valid@email.com');
    expect(emailControl?.valid).toBeTruthy();
  });
});
```

**2. Service Tests**
```typescript
describe('AuthService', () => {
  let service: AuthService;
  let httpMock: HttpTestingController;
  
  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [AuthService]
    });
    
    service = TestBed.inject(AuthService);
    httpMock = TestBed.inject(HttpTestingController);
  });
  
  it('should login successfully', () => {
    const mockResponse = {
      accessToken: 'fake-token',
      userId: 1,
      username: 'test@example.com'
    };
    
    service.login('test@example.com', 'password').subscribe(response => {
      expect(response.accessToken).toBe('fake-token');
    });
    
    const req = httpMock.expectOne(`${service.baseUrl}/login`);
    expect(req.request.method).toBe('POST');
    req.flush(mockResponse);
  });
  
  afterEach(() => {
    httpMock.verify();
  });
});
```

---

## 🚀 Future Improvements

### **Planned Features**

1. **Enhanced Authentication**
   - ✅ Implement bcrypt password encryption
   - ✅ Add refresh token mechanism
   - ✅ OAuth 2.0 integration (Google, GitHub)
   - ✅ Email verification
   - ✅ Password reset functionality

2. **Database Migration**
   - ✅ Replace H2 with PostgreSQL/MySQL for production
   - ✅ Add connection pooling (HikariCP)
   - ✅ Implement database backups
   - ✅ Add database indexing for performance

3. **Code Execution**
   - ✅ Real code execution using Docker containers
   - ✅ Integration with Judge0 API
   - ✅ Support for more languages (Go, Rust, Swift)
   - ✅ Test case validation
   - ✅ Time and memory limit enforcement

4. **Advanced Visualizations**
   - ✅ 3D algorithm visualizations
   - ✅ GPU-accelerated animations
   - ✅ Custom speed controls
   - ✅ Pause/resume at any step
   - ✅ Breakpoint system for debugging

5. **Gamification Enhancements**
   - ✅ More badges (10+ → 50+)
   - ✅ Achievement unlocks
   - ✅ Daily challenges
   - ✅ Streak system
   - ✅ XP and leveling system
   - ✅ Clan/team competitions

6. **Social Features**
   - ✅ User profiles
   - ✅ Follow/friend system
   - ✅ Code sharing
   - ✅ Discussion forums
   - ✅ Algorithm challenges

7. **Performance Optimization**
   - ✅ Lazy loading for routes
   - ✅ Service workers for PWA
   - ✅ Code splitting
   - ✅ CDN for static assets
   - ✅ Redis caching layer

8. **Monitoring & Analytics**
   - ✅ Application performance monitoring (APM)
   - ✅ Error tracking (Sentry)
   - ✅ User analytics (Google Analytics)
   - ✅ Custom dashboards
   - ✅ API rate limiting

9. **Mobile App**
   - ✅ React Native/Flutter mobile version
   - ✅ Offline mode
   - ✅ Push notifications
   - ✅ Touch-optimized visualizations

10. **AI Integration**
    - ✅ AI-powered code suggestions
    - ✅ Personalized learning paths
    - ✅ Automated quiz generation
    - ✅ Smart hints during visualization

---

## 📦 Deployment

### **Development**
```bash
# Backend
cd backend
./mvnw spring-boot:run

# Frontend
cd frontend
npm install
ng serve
```

### **Production**
```bash
# Backend
./mvnw clean package
java -jar target/backend-0.0.1-SNAPSHOT.jar

# Frontend
ng build --configuration production
# Deploy dist/ to Netlify/Vercel

# Docker
docker-compose up -d
```

---

## 📊 Database Schema

### **Key Entities**

**Users**
- id, uuid, email, displayName, passwordHash, role, createdAt, lastActive

**Algorithms**
- id, name, category (DSA/DAA), difficulty, description

**UserProgress**
- id, userId, algorithmId, timeSpent, stepsCompleted, quizScore, isCompleted

**Quizzes**
- id, algorithmId, question, optionA-D, correctAnswer, explanation

**QuizAttempts**
- id, userId, quizId, userAnswer, isCorrect, attemptedAt

**Badges**
- id, name, description, icon, criteria, isActive

**UserBadges**
- id, userId, badgeId, awardedAt

---

## 🎓 Learning Outcomes

Students using this platform will:
- ✅ Understand algorithm complexities
- ✅ Visualize step-by-step execution
- ✅ Practice with interactive quizzes
- ✅ Write and test code
- ✅ Track their progress
- ✅ Compete on leaderboards
- ✅ Earn badges and achievements

---

## 📄 License

MIT License - Free for educational use

---

## 👥 Contributors

**Shivani (Shubh)** - Full Stack Developer

---

**Last Updated:** October 20, 2025
