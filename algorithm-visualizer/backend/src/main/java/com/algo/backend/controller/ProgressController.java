package com.algo.backend.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.algo.backend.entity.Algorithm;
import com.algo.backend.entity.AlgorithmCategory;
import com.algo.backend.entity.User;
import com.algo.backend.entity.UserProgress;
import com.algo.backend.repository.AlgorithmCategoryRepository;
import com.algo.backend.repository.AlgorithmRepository;
import com.algo.backend.repository.UserProgressRepository;
import com.algo.backend.repository.UserRepository;
import com.algo.backend.util.JwtUtil;

@RestController
@RequestMapping("/api/progress")
@CrossOrigin(origins = {"http://localhost:4200", "http://localhost:4201"})
public class ProgressController {

    @Autowired
    private UserProgressRepository progressRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AlgorithmRepository algorithmRepository;

    @Autowired
    private AlgorithmCategoryRepository algorithmCategoryRepository;

    @Autowired
    private JwtUtil jwtUtil;

    // -------------------- Helper Methods --------------------
    private String extractEmailFromAuthHeader(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) return null;
        try {
            String token = authHeader.substring(7);
            return jwtUtil.getEmailFromToken(token);
        } catch (Exception e) {
            return null;
        }
    }

    private User getUserFromToken(String authHeader) {
        String email = extractEmailFromAuthHeader(authHeader);
        if (email == null) return null;
        return userRepository.findByUsername(email).orElse(null);
    }

    private int parseInteger(Object value) {
        if (value instanceof Number num) return num.intValue();
        if (value == null) return 0;
        try {
            return Integer.parseInt(value.toString());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private long parseLong(Object value) {
        if (value instanceof Number num) return num.longValue();
        if (value == null) return 0L;
        try {
            return Long.parseLong(value.toString());
        } catch (NumberFormatException e) {
            return 0L;
        }
    }

    private Map<String, Object> createDefaultProgress(String algorithmId) {
        Map<String, Object> response = new HashMap<>();
        response.put("algorithmId", algorithmId);
        response.put("algorithmName", algorithmId);
        response.put("completed", false);
        response.put("stepsCompleted", 0);
        response.put("totalSteps", 0);
        response.put("timeSpent", 0);
        response.put("lastAccessed", null);
        response.put("difficulty", "medium");
        response.put("startTime", System.currentTimeMillis());
        return response;
    }

    private Map<String, Object> createDefaultUserProgress() {
        Map<String, Object> response = new HashMap<>();
        response.put("userId", null);
        response.put("algorithmsCompleted", new ArrayList<>());
        response.put("totalAlgorithms", 0);
        response.put("completedAlgorithms", 0);
        response.put("averageQuizScore", 0.0);
        response.put("totalTimeSpent", 0);
        response.put("lastActive", null);
        return response;
    }

    private int calculateStreak(List<UserProgress> progressList) {
        return !progressList.isEmpty() ? Math.min(progressList.size(), 30) : 0;
    }

    private Algorithm getOrCreateAlgorithm(String algorithmName, String defaultCategoryName) {
        return algorithmRepository.findByName(algorithmName).orElseGet(() -> {
            Algorithm algo = new Algorithm();
            algo.setName(algorithmName);
            AlgorithmCategory category = algorithmCategoryRepository.findByName(defaultCategoryName)
                    .orElseGet(() -> {
                        AlgorithmCategory cat = new AlgorithmCategory();
                        cat.setName(defaultCategoryName);
                        cat.setSlug(defaultCategoryName.toLowerCase());
                        return algorithmCategoryRepository.save(cat);
                    });
            algo.setCategory(category);
            return algorithmRepository.save(algo);
        });
    }

    // -------------------- Endpoints --------------------

    @GetMapping("/{userEmail}/{algorithmId}")
    public ResponseEntity<?> getUserAlgorithmProgress(@PathVariable String userEmail,
                                                      @PathVariable String algorithmId) {
        User user = userRepository.findByEmail(userEmail).orElse(null);
        Algorithm algorithm = algorithmRepository.findByName(algorithmId).orElse(null);
        if (user == null || algorithm == null) return ResponseEntity.ok(createDefaultProgress(algorithmId));

        UserProgress progress = progressRepository.findByUserAndAlgorithm(user, algorithm).orElse(null);
        if (progress == null) return ResponseEntity.ok(createDefaultProgress(algorithmId));

        Map<String, Object> response = new HashMap<>();
        response.put("algorithmId", algorithmId);
        response.put("algorithmName", algorithm.getName());
        response.put("completed", progress.getIsCompleted());
        response.put("stepsCompleted", parseInteger(progress.getStepsCompleted()));
        response.put("totalSteps", parseInteger(progress.getTotalSteps()));
        response.put("timeSpent", (int) parseLong(progress.getTimeSpentSeconds()));
        response.put("lastAccessed", progress.getUpdatedAt());
        response.put("difficulty", progress.getDifficulty() != null ? progress.getDifficulty() : "medium");
        response.put("startTime", System.currentTimeMillis());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/user")
    public ResponseEntity<?> getUserProgress(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        User user = getUserFromToken(authHeader);
        if (user == null) return ResponseEntity.ok(createDefaultUserProgress());

        List<UserProgress> progressList = progressRepository.findByUser(user);
        Map<String, Object> response = new HashMap<>();
        response.put("userId", user.getId());
        response.put("algorithmsCompleted", progressList.stream()
                .filter(UserProgress::getIsCompleted)
                .map(p -> p.getAlgorithm().getName())
                .collect(Collectors.toList()));
        response.put("totalAlgorithms", progressList.size());
        response.put("completedAlgorithms", (int) progressList.stream().filter(UserProgress::getIsCompleted).count());
        response.put("averageQuizScore", progressList.stream()
                .filter(p -> p.getBestScore() != null)
                .mapToInt(UserProgress::getBestScore)
                .average()
                .orElse(0.0));
        response.put("totalTimeSpent", progressList.stream()
                .mapToInt(p -> (int) parseLong(p.getTimeSpentSeconds()))
                .sum());
        response.put("lastActive", user.getUpdatedAt());

        return ResponseEntity.ok(response);
    }

    @PostMapping("/{userEmail}")
    public ResponseEntity<?> saveVisualizationProgress(@PathVariable String userEmail,
                                                       @RequestBody Map<String, Object> request) {
        User user = userRepository.findByEmail(userEmail).orElseGet(() -> {
            User u = new User();
            u.setEmail(userEmail);
            u.setDisplayName(userEmail.split("@")[0]);
            return userRepository.save(u);
        });

        String algorithmId = (String) request.get("algorithmId");
        Algorithm algorithm = getOrCreateAlgorithm(algorithmId, "Sorting");

        UserProgress progress = progressRepository.findByUserAndAlgorithm(user, algorithm)
                .orElse(new UserProgress(user, algorithm));

        Boolean completed = (Boolean) request.getOrDefault("completed", false);
        progress.setIsCompleted(completed);
        progress.setStepsCompleted(parseInteger(request.get("stepsCompleted")));
        progress.setTotalSteps(parseInteger(request.get("totalSteps")));
        progress.setTimeSpent((int) parseLong(request.get("timeSpent")));
        progress.setDifficulty((String) request.getOrDefault("difficulty", "medium"));
        progress.setUpdatedAt(LocalDateTime.now());
        if (completed) progress.setCompletedAt(LocalDateTime.now());

        progressRepository.save(progress);
        return ResponseEntity.ok(Map.of("message", "Progress saved successfully"));
    }

    @PostMapping("/save")
    public ResponseEntity<?> saveExerciseProgress(@RequestHeader(value = "Authorization", required = false) String authHeader,
                                                  @RequestBody Map<String, Object> request) {
        User user = getUserFromToken(authHeader);
        if (user == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("message", "User not authenticated"));

        int totalScore = parseInteger(request.get("totalScore"));
        Algorithm exerciseAlgo = getOrCreateAlgorithm("Mixed Exercises", "Exercises");
        UserProgress progress = progressRepository.findByUserAndAlgorithm(user, exerciseAlgo)
                .orElse(new UserProgress(user, exerciseAlgo));

        progress.setBestScore(totalScore);
        progress.setIsCompleted(totalScore > 0);
        if (totalScore > 0) progress.setCompletedAt(LocalDateTime.now());
        progress.setAttemptsCount(progress.getAttemptsCount() + 1);
        progressRepository.save(progress);

        return ResponseEntity.ok(Map.of("message", "Exercise progress saved successfully", "status", "success"));
    }

    @PostMapping("/exercise")
    public ResponseEntity<?> updateExerciseProgress(@RequestHeader(value = "Authorization", required = false) String authHeader,
                                                    @RequestBody Map<String, Object> request) {
        return saveExerciseProgress(authHeader, request);
    }

    @GetMapping("/leaderboard")
    public ResponseEntity<?> getLeaderboard(@RequestParam(defaultValue = "all") String filter,
                                            @RequestParam(defaultValue = "1") int page,
                                            @RequestParam(defaultValue = "10") int size) {
        List<User> users = userRepository.findAll();
        List<Map<String, Object>> leaderboardData = new ArrayList<>();

        for (User user : users) {
            List<UserProgress> userProgress = progressRepository.findByUser(user);

            int totalScore = userProgress.stream()
                    .filter(p -> p.getBestScore() != null)
                    .mapToInt(UserProgress::getBestScore)
                    .sum();

            int algorithmsCompleted = (int) userProgress.stream()
                    .filter(UserProgress::getIsCompleted)
                    .count();

            int mcqScore = userProgress.stream()
                    .filter(p -> p.getAlgorithm().getName().startsWith("exercise_mcq"))
                    .mapToInt(p -> Objects.requireNonNullElse(p.getBestScore(), 0))
                    .sum();

            int codingScore = userProgress.stream()
                    .filter(p -> p.getAlgorithm().getName().startsWith("exercise_coding"))
                    .mapToInt(p -> Objects.requireNonNullElse(p.getBestScore(), 0))
                    .sum();

            Map<String, Object> userData = new HashMap<>();
            userData.put("id", user.getId());
            userData.put("username", user.getDisplayName());
            userData.put("totalScore", totalScore);
            userData.put("mcqScore", mcqScore);
            userData.put("codingScore", codingScore);
            userData.put("algorithmsCompleted", algorithmsCompleted);
            userData.put("streak", calculateStreak(userProgress));
            userData.put("lastActive", user.getUpdatedAt());
            userData.put("avatar", null);

            leaderboardData.add(userData);
        }

        leaderboardData.sort((a, b) -> Integer.compare((Integer) b.get("totalScore"), (Integer) a.get("totalScore")));
        for (int i = 0; i < leaderboardData.size(); i++) {
            leaderboardData.get(i).put("rank", i + 1);
        }

        int start = (page - 1) * size;
        int end = Math.min(start + size, leaderboardData.size());
        List<Map<String, Object>> paginatedData = leaderboardData.subList(start, end);

        Map<String, Object> response = new HashMap<>();
        response.put("data", paginatedData);
        response.put("totalUsers", users.size());
        response.put("totalPages", (int) Math.ceil((double) leaderboardData.size() / size));
        response.put("currentPage", page);
        response.put("averageScore", leaderboardData.stream()
                .mapToInt(u -> (Integer) u.get("totalScore"))
                .average()
                .orElse(0.0));

        return ResponseEntity.ok(response);
    }
}
