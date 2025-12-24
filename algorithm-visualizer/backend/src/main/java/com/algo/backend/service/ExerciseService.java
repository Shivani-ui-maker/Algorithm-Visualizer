package com.algo.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.algo.backend.entity.Exercise;
import com.algo.backend.entity.ExerciseSubmission;
import com.algo.backend.repository.ExerciseRepository;
import com.algo.backend.repository.ExerciseSubmissionRepository;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

@Service
public class ExerciseService {

    @Autowired
    private ExerciseRepository exerciseRepository;

    @Autowired
    private ExerciseSubmissionRepository submissionRepository;


    // Get all exercises
    public List<Exercise> getAllExercises() {
        return exerciseRepository.findAll();
    }

    // Get exercises by algorithm
    public List<Exercise> getExercisesByAlgorithm(Long algorithmId) {
        return exerciseRepository.findByAlgorithmId(algorithmId);
    }

    // Get exercises by difficulty
    public List<Exercise> getExercisesByDifficulty(String difficulty) {
        return exerciseRepository.findByDifficulty(Exercise.Difficulty.valueOf(difficulty.toLowerCase()));
    }

    // Get exercises by category
    public List<Exercise> getExercisesByCategory(String category) {
        return exerciseRepository.findByAlgorithmCategoryName(category);
    }

    // Get exercise by ID
    public Exercise getExerciseById(Long exerciseId) {
        return exerciseRepository.findById(exerciseId)
                .orElseThrow(() -> new RuntimeException("Exercise not found"));
    }

    // Evaluate code submission
    public Map<String, Object> evaluateSubmission(Long exerciseId, String code, String language, String userId) {
        Exercise exercise = getExerciseById(exerciseId);
        
        Map<String, Object> result = new HashMap<>();
        
        // Simple code evaluation logic (can be enhanced with actual code execution)
        boolean passed = evaluateCode(code, language, exercise);
        
        ExerciseSubmission submission = new ExerciseSubmission();
        submission.setExercise(exercise);
        submission.setCode(code);
        submission.setLanguage(ExerciseSubmission.Language.valueOf(language.toLowerCase()));
        submission.setGuestSessionId(userId); // Store as guest session for now
        submission.setPassed(passed);
        submission.setStatus(passed ? ExerciseSubmission.Status.PASSED : ExerciseSubmission.Status.FAILED);
        
        submissionRepository.save(submission);
        
        result.put("passed", passed);
        result.put("message", passed ? "Solution accepted!" : "Solution failed. Try again.");
        result.put("submissionId", submission.getId());
        
        return result;
    }

    // Get user submissions for an exercise
    public List<ExerciseSubmission> getUserSubmissions(Long exerciseId, String userId) {
        return submissionRepository.findByExerciseIdAndGuestSessionId(exerciseId, userId);
    }

    // Get total submissions for an exercise
    public long getTotalSubmissions(Long exerciseId) {
        return submissionRepository.countByExerciseId(exerciseId);
    }

    // Get success rate for an exercise
    public double getSuccessRate(Long exerciseId) {
        long total = getTotalSubmissions(exerciseId);
        if (total == 0) return 0.0;
        
        long passed = submissionRepository.countByExerciseIdAndPassed(exerciseId, true);
        return (double) passed / total * 100;
    }

    // Get average attempts for an exercise
    public double getAverageAttempts(Long exerciseId) {
        return submissionRepository.getAverageAttemptsByExerciseId(exerciseId);
    }

    // Get completed exercises count for user
    public long getCompletedExercises(String userId) {
        try {
            // Convert string userId to Long for the repository call
            Long userIdLong = Long.parseLong(userId);
            return submissionRepository.countDistinctExercisesByUserIdAndPassed(userIdLong, true);
        } catch (NumberFormatException e) {
            // If userId is not a valid Long, return 0
            return 0;
        }
    }

    // Get total exercises count
    public long getTotalExercises() {
        return exerciseRepository.count();
    }

    // Get user success rate
    public double getUserSuccessRate(String userId) {
        try {
            // Convert string userId to Long for the repository call
            Long userIdLong = Long.parseLong(userId);
            long totalAttempts = submissionRepository.countDistinctExercisesByUserIdAndPassed(userIdLong, true) + 
                                submissionRepository.countDistinctExercisesByUserIdAndPassed(userIdLong, false);
            if (totalAttempts == 0) return 0.0;
            
            long passedAttempts = submissionRepository.countDistinctExercisesByUserIdAndPassed(userIdLong, true);
            return (double) passedAttempts / totalAttempts * 100;
        } catch (NumberFormatException e) {
            // If userId is not a valid Long, return 0.0
            return 0.0;
        }
    }

    // Get user streak
    public int getUserStreak(String userId) {
        try {
            // Convert string userId to Long for the repository call
            Long userIdLong = Long.parseLong(userId);
            Long streak = submissionRepository.getCurrentStreakByUserId(userIdLong);
            return streak != null ? streak.intValue() : 0;
        } catch (NumberFormatException e) {
            // If userId is not a valid Long, return 0 streak
            return 0;
        }
    }

    // Simple code evaluation logic
    private boolean evaluateCode(String code, String language, Exercise exercise) {
        // Basic validation - check if code contains expected keywords/patterns
        if (code == null || code.trim().isEmpty()) {
            return false;
        }
        
        // Simple heuristic based on exercise title and code content
        String exerciseTitle = exercise.getTitle().toLowerCase();
        String codeContent = code.toLowerCase();
        
        if (exerciseTitle.contains("bubble sort")) {
            return codeContent.contains("for") && codeContent.contains("swap");
        } else if (exerciseTitle.contains("binary search")) {
            return codeContent.contains("while") && codeContent.contains("mid");
        } else if (exerciseTitle.contains("stack")) {
            return codeContent.contains("push") || codeContent.contains("pop");
        } else if (exerciseTitle.contains("queue")) {
            return codeContent.contains("enqueue") || codeContent.contains("dequeue");
        }
        
        // Default: accept if code has reasonable length and structure
        return code.length() > 50 && (codeContent.contains("return") || codeContent.contains("print"));
    }
}

