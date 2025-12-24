package com.algo.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.algo.backend.entity.Exercise;
import com.algo.backend.entity.ExerciseSubmission;
import com.algo.backend.service.ExerciseService;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/exercises")
@CrossOrigin(origins = {"http://localhost:4200", "http://localhost:4201"})
public class ExerciseController {

    @Autowired
    private ExerciseService exerciseService;

    // Get all exercises
    @GetMapping
    public ResponseEntity<List<Exercise>> getAllExercises() {
        return ResponseEntity.ok(exerciseService.getAllExercises());
    }

    // Get exercises by algorithm
    @GetMapping("/algorithm/{algorithmId}")
    public ResponseEntity<List<Exercise>> getByAlgorithm(@PathVariable Long algorithmId) {
        return ResponseEntity.ok(exerciseService.getExercisesByAlgorithm(algorithmId));
    }

    // Get exercises by difficulty (easy, medium, hard)
    @GetMapping("/difficulty/{level}")
    public ResponseEntity<List<Exercise>> getByDifficulty(@PathVariable String level) {
        return ResponseEntity.ok(exerciseService.getExercisesByDifficulty(level.toUpperCase()));
    }

    // Get exercises by category (DSA, DAA)
    @GetMapping("/category/{category}")
    public ResponseEntity<List<Exercise>> getByCategory(@PathVariable String category) {
        return ResponseEntity.ok(exerciseService.getExercisesByCategory(category.toUpperCase()));
    }

    // Get exercise by ID
    @GetMapping("/{exerciseId}")
    public ResponseEntity<Exercise> getExerciseById(@PathVariable Long exerciseId) {
        return ResponseEntity.ok(exerciseService.getExerciseById(exerciseId));
    }

    // Submit code solution
    @PostMapping("/{exerciseId}/submit")
    public ResponseEntity<Map<String, Object>> submitSolution(
            @PathVariable Long exerciseId,
            @RequestBody Map<String, Object> submission) {
        
        String code = (String) submission.get("code");
        String language = (String) submission.get("language");
        String userId = (String) submission.get("userId");
        
        Map<String, Object> result = exerciseService.evaluateSubmission(exerciseId, code, language, userId);
        return ResponseEntity.ok(result);
    }

    // Get user submissions for an exercise
    @GetMapping("/{exerciseId}/submissions/{userId}")
    public ResponseEntity<List<ExerciseSubmission>> getUserSubmissions(
            @PathVariable Long exerciseId,
            @PathVariable String userId) {
        return ResponseEntity.ok(exerciseService.getUserSubmissions(exerciseId, userId));
    }

    // Get exercise statistics
    @GetMapping("/{exerciseId}/stats")
    public ResponseEntity<Map<String, Object>> getExerciseStats(@PathVariable Long exerciseId) {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalSubmissions", exerciseService.getTotalSubmissions(exerciseId));
        stats.put("successRate", exerciseService.getSuccessRate(exerciseId));
        stats.put("averageAttempts", exerciseService.getAverageAttempts(exerciseId));
        return ResponseEntity.ok(stats);
    }

    // Get user's exercise progress
    @GetMapping("/progress/{userId}")
    public ResponseEntity<Map<String, Object>> getUserProgress(@PathVariable String userId) {
        Map<String, Object> progress = new HashMap<>();
        progress.put("completedExercises", exerciseService.getCompletedExercises(userId));
        progress.put("totalExercises", exerciseService.getTotalExercises());
        progress.put("successRate", exerciseService.getUserSuccessRate(userId));
        progress.put("streak", exerciseService.getUserStreak(userId));
        return ResponseEntity.ok(progress);
    }
}

