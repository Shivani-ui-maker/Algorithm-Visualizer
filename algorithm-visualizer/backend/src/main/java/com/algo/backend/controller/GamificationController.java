package com.algo.backend.controller;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;               // use the util we kept
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.algo.backend.entity.User;
import com.algo.backend.entity.UserProgress;
import com.algo.backend.repository.UserProgressRepository;
import com.algo.backend.repository.UserRepository;
import com.algo.backend.util.JwtUtil;

@RestController
@RequestMapping("/api/gamification")
@CrossOrigin(origins = "http://localhost:4200")
public class GamificationController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserProgressRepository progressRepository;

    @Autowired
    private JwtUtil jwtUtil;

    // Helper to extract token and convert to email/username safely
    private Optional<String> extractEmailFromAuthHeader(String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return Optional.empty();
        }
        String jwt = authorizationHeader.substring(7).trim();
        if (jwt.isEmpty() || !jwtUtil.validateToken(jwt)) {
            return Optional.empty();
        }
        return Optional.ofNullable(jwtUtil.getEmailFromToken(jwt));
    }

    @GetMapping("/stats")
    public ResponseEntity<?> getUserStats(@RequestHeader(value = "Authorization", required = false) String authorization) {
        Optional<String> maybeEmail = extractEmailFromAuthHeader(authorization);
        if (maybeEmail.isEmpty()) {
            return ResponseEntity.status(401).body(Map.of("error", "Invalid or missing Authorization token"));
        }
        String usernameOrEmail = maybeEmail.get();

        User user = userRepository.findByUsername(usernameOrEmail).orElse(null);
        if (user == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "User not found"));
        }

        List<UserProgress> progressList = progressRepository.findByUser(user);

        int totalScore = progressList.stream()
                .map(UserProgress::getBestScore)
                .filter(Objects::nonNull)
                .mapToInt(Integer::intValue)
                .sum();

        long algorithmsCompleted = progressList.stream()
                .filter(p -> p.getStatus() == UserProgress.Status.COMPLETED)
                .count();

        long exercisesCompleted = progressList.stream()
                .filter(p -> p.getAlgorithm() != null && p.getAlgorithm().getName() != null)
                .filter(p -> p.getAlgorithm().getName().startsWith("exercise_"))
                .filter(p -> p.getStatus() == UserProgress.Status.COMPLETED)
                .count();

        int streak = calculateStreak(user);
        int level = calculateLevel(totalScore);
        int xp = totalScore * 10; // 10 XP per score point
        int nextLevelXp = calculateXPForNextLevel(level);

        List<Map<String, Object>> badges = getBadgesForUser(user, progressList);

        Map<String, Object> stats = new HashMap<>();
        stats.put("totalScore", totalScore);
        stats.put("streak", streak);
        stats.put("badges", badges);
        stats.put("level", level);
        stats.put("xp", xp);
        stats.put("nextLevelXp", nextLevelXp);
        stats.put("algorithmsCompleted", algorithmsCompleted);
        stats.put("exercisesCompleted", exercisesCompleted);

        return ResponseEntity.ok(stats);
    }

    @PostMapping("/add-xp")
    public ResponseEntity<?> addXP(@RequestHeader(value = "Authorization", required = false) String authorization,
                                   @RequestBody Map<String, Object> request) {
        Optional<String> maybeEmail = extractEmailFromAuthHeader(authorization);
        if (maybeEmail.isEmpty()) {
            return ResponseEntity.status(401).body(Map.of("error", "Invalid or missing Authorization token"));
        }
        String usernameOrEmail = maybeEmail.get();
        User user = userRepository.findByUsername(usernameOrEmail).orElse(null);
        if (user == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "User not found"));
        }

        // Safely extract numeric amount (could be Integer, Long, Double depending on client)
        Object amountObj = request.get("amount");
        int amount = 0;
        if (amountObj instanceof Number) {
            amount = ((Number) amountObj).intValue();
        } else if (amountObj instanceof String) {
            try {
                amount = Integer.parseInt((String) amountObj);
            } catch (NumberFormatException ignored) { /* keep amount = 0 */ }
        }
        String source = Optional.ofNullable(request.get("source")).map(Object::toString).orElse("unknown");

        // Log XP addition and return summary (persistence not implemented here)
        Map<String, Object> xpData = new HashMap<>();
        xpData.put("amount", amount);
        xpData.put("source", source);
        xpData.put("timestamp", LocalDateTime.now());

        return ResponseEntity.ok(Map.of("message", "XP added successfully", "data", xpData));
    }

    @PostMapping("/check-badges")
    public ResponseEntity<?> checkBadges(@RequestHeader(value = "Authorization", required = false) String authorization,
                                         @RequestBody(required = false) Map<String, Object> request) {
        Optional<String> maybeEmail = extractEmailFromAuthHeader(authorization);
        if (maybeEmail.isEmpty()) {
            return ResponseEntity.status(401).body(Map.of("error", "Invalid or missing Authorization token"));
        }
        String usernameOrEmail = maybeEmail.get();
        User user = userRepository.findByUsername(usernameOrEmail).orElse(null);
        if (user == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "User not found"));
        }

        List<UserProgress> progressList = progressRepository.findByUser(user);
        List<Map<String, Object>> newBadges = checkForNewBadges(user, progressList);
        return ResponseEntity.ok(newBadges);
    }

    @PostMapping("/update-streak")
    public ResponseEntity<?> updateStreak(@RequestHeader(value = "Authorization", required = false) String authorization) {
        Optional<String> maybeEmail = extractEmailFromAuthHeader(authorization);
        if (maybeEmail.isEmpty()) {
            return ResponseEntity.status(401).body(Map.of("error", "Invalid or missing Authorization token"));
        }
        String usernameOrEmail = maybeEmail.get();
        User user = userRepository.findByUsername(usernameOrEmail).orElse(null);
        if (user == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "User not found"));
        }

        // Update user's last active time
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);

        int newStreak = calculateStreak(user);
        return ResponseEntity.ok(Map.of("streak", newStreak));
    }

    private int calculateLevel(int totalScore) {
        return (int) Math.floor(Math.sqrt(totalScore / 100.0)) + 1;
    }

    private int calculateXPForNextLevel(int currentLevel) {
        return currentLevel * currentLevel * 100;
    }

    private int calculateStreak(User user) {
        LocalDateTime lastActive = user.getUpdatedAt();
        LocalDateTime now = LocalDateTime.now();

        if (lastActive == null) return 0;

        long daysSinceLastActive = ChronoUnit.DAYS.between(lastActive.toLocalDate(), now.toLocalDate());

        if (daysSinceLastActive <= 1) {
            // If lastActive is today or yesterday, keep streak. (This logic can be adapted)
            return Math.min(30, (int) daysSinceLastActive + 1);
        } else {
            return 0;
        }
    }

    private List<Map<String, Object>> getBadgesForUser(User user, List<UserProgress> progressList) {
        List<Map<String, Object>> badges = new ArrayList<>();

        long algorithmsCompleted = progressList.stream()
                .filter(p -> p.getStatus() == UserProgress.Status.COMPLETED)
                .count();

        long perfectQuizzes = progressList.stream()
                .filter(p -> p.getBestScore() != null && p.getBestScore() >= 100)
                .count();

        if (algorithmsCompleted >= 1) {
            badges.add(createBadge("first_algorithm", "First Steps", "Complete your first algorithm", "bi-award", "#4caf50"));
        }

        if (perfectQuizzes >= 5) {
            badges.add(createBadge("quiz_champion", "Quiz Champion", "Score 100% on 5 quizzes", "bi-trophy", "#ffd700"));
        }

        if (algorithmsCompleted >= 10) {
            badges.add(createBadge("algorithm_explorer", "Algorithm Explorer", "Complete 10 algorithms", "bi-compass", "#00bcd4"));
        }

        return badges;
    }

    private List<Map<String, Object>> checkForNewBadges(User user, List<UserProgress> progressList) {
        // Database-backed badge tracking - returns empty list for now
        return new ArrayList<>();
    }

    private Map<String, Object> createBadge(String id, String name, String description, String icon, String color) {
        Map<String, Object> badge = new HashMap<>();
        badge.put("id", id);
        badge.put("name", name);
        badge.put("description", description);
        badge.put("icon", icon);
        badge.put("color", color);
        badge.put("unlockedAt", LocalDateTime.now());
        return badge;
    }
}
