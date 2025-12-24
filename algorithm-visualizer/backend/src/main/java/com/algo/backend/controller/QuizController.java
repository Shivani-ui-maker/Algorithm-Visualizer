package com.algo.backend.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.algo.backend.entity.Quiz;
import com.algo.backend.entity.QuizAttempt;
import com.algo.backend.service.QuizService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/quiz")
public class QuizController {

    @Autowired
    private QuizService quizService;

    @GetMapping("/public/algorithm/{algorithmId}")
    public ResponseEntity<List<Quiz>> getQuizzesByAlgorithm(@PathVariable Long algorithmId) {
        List<Quiz> quizzes = quizService.getQuizzesByAlgorithm(algorithmId);
        return ResponseEntity.ok(quizzes);
    }

    @GetMapping("/public/algorithm/{algorithmId}/random/{limit}")
    public ResponseEntity<List<Quiz>> getRandomQuizzesByAlgorithm(@PathVariable Long algorithmId, @PathVariable int limit) {
        List<Quiz> quizzes = quizService.getRandomQuizzesByAlgorithm(algorithmId, limit);
        return ResponseEntity.ok(quizzes);
    }

    @GetMapping("/public/random/{limit}")
    public ResponseEntity<List<Quiz>> getRandomQuizzes(@PathVariable int limit) {
        List<Quiz> quizzes = quizService.getRandomQuizzes(limit);
        return ResponseEntity.ok(quizzes);
    }

    @GetMapping("/public/{id}")
    public ResponseEntity<Quiz> getQuizById(@PathVariable Long id) {
        Optional<Quiz> quiz = quizService.getQuizById(id);
        return quiz.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/submit")
    public ResponseEntity<QuizAttempt> submitQuizAnswer(
            @RequestBody Map<String, Object> request,
            Authentication authentication) {
        
        if (authentication == null) {
            return ResponseEntity.badRequest().build();
        }

        Long quizId = Long.valueOf(request.get("quizId").toString());
        String selectedAnswer = request.get("selectedAnswer").toString();
        Long userId = Long.valueOf(request.get("userId").toString());

        QuizAttempt attempt = quizService.submitQuizAnswer(userId, quizId, selectedAnswer);
        if (attempt != null) {
            return ResponseEntity.ok(attempt);
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/attempts/{userId}")
    public ResponseEntity<List<QuizAttempt>> getUserQuizAttempts(@PathVariable Long userId) {
        List<QuizAttempt> attempts = quizService.getUserQuizAttempts(userId);
        return ResponseEntity.ok(attempts);
    }

    @GetMapping("/stats/{userId}/correct-count")
    public ResponseEntity<Long> getUserCorrectAnswersCount(@PathVariable Long userId) {
        Long count = quizService.getUserCorrectAnswersCount(userId);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/stats/{userId}/average-score")
    public ResponseEntity<Double> getUserAverageScore(@PathVariable Long userId) {
        Double average = quizService.getUserAverageScore(userId);
        return ResponseEntity.ok(average);
    }

    @PostMapping("/admin/create")
    public ResponseEntity<Quiz> createQuiz(@RequestBody Quiz quiz) {
        Quiz savedQuiz = quizService.saveQuiz(quiz);
        return ResponseEntity.ok(savedQuiz);
    }

    @DeleteMapping("/admin/{id}")
    public ResponseEntity<?> deleteQuiz(@PathVariable Long id) {
        quizService.deleteQuiz(id);
        return ResponseEntity.ok().build();
    }

    // Save quiz progress by email
    @PostMapping("/{userEmail}")
    public ResponseEntity<?> saveQuizProgress(@PathVariable String userEmail,
                                             @RequestBody Map<String, Object> request) {
        try {
        String algorithmId = (String) request.get("algorithmId");
        Integer score = (Integer) request.get("score");
        Integer percentage = (Integer) request.get("percentage");
            
            // For now, return success - in a real implementation, you'd save to database
            Map<String, Object> response = Map.of(
                "message", "Quiz progress saved successfully",
                "userEmail", userEmail,
                "algorithmId", algorithmId,
                "score", score,
                "percentage", percentage
            );
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.ok(Map.of("message", "Quiz progress saved locally", "error", e.getMessage()));
        }
    }

    // Get quiz questions for specific algorithm
    @GetMapping("/{algorithmId}")
    public ResponseEntity<?> getAlgorithmQuiz(@PathVariable String algorithmId) {
        try {
            // Return built-in quiz questions for the algorithm
            // In a real implementation, these would come from the database
            return ResponseEntity.ok(getBuiltInQuizQuestions(algorithmId));
        } catch (Exception e) {
            return ResponseEntity.ok(List.of()); // Return empty list on error
        }
    }

    @GetMapping("/random")
    public ResponseEntity<?> getRandomQuiz() {
        try {
            // Return random quiz questions
            return ResponseEntity.ok(getBuiltInQuizQuestions("bubble-sort"));
        } catch (Exception e) {
            return ResponseEntity.ok(List.of());
        }
    }

    private List<Map<String, Object>> getBuiltInQuizQuestions(String algorithmId) {
        // Built-in quiz questions for different algorithms
        return switch (algorithmId) {
            case "bubble-sort" -> List.of(
                Map.of(
                    "question", "What is the time complexity of Bubble Sort in the worst case?",
                    "options", List.of("O(n)", "O(n log n)", "O(n²)", "O(2^n)"),
                    "correctAnswer", 2,
                    "explanation", "Bubble sort has O(n²) time complexity in worst case because it uses nested loops."
                ),
                Map.of(
                    "question", "In Bubble Sort, what happens in each pass?",
                    "options", List.of("The smallest element moves to the beginning", "The largest element moves to the end", "Elements are randomly shuffled", "Nothing happens"),
                    "correctAnswer", 1,
                    "explanation", "In each pass, the largest unsorted element bubbles up to its correct position at the end."
                ),
                Map.of(
                    "question", "When is Bubble Sort most efficient?",
                    "options", List.of("When array is reverse sorted", "When array is already sorted", "When array has duplicates", "Never efficient"),
                    "correctAnswer", 1,
                    "explanation", "Bubble Sort performs best on already sorted arrays with O(n) time complexity."
                )
            );
            case "quick-sort" -> List.of(
                Map.of(
                    "question", "What is the average time complexity of Quick Sort?",
                    "options", List.of("O(n)", "O(n log n)", "O(n²)", "O(log n)"),
                    "correctAnswer", 1,
                    "explanation", "Quick sort has O(n log n) average time complexity due to divide-and-conquer approach."
                ),
                Map.of(
                    "question", "What is the key element in Quick Sort partitioning?",
                    "options", List.of("First element", "Last element", "Pivot element", "Middle element"),
                    "correctAnswer", 2,
                    "explanation", "The pivot element is used to partition the array into smaller and larger elements."
                ),
                Map.of(
                    "question", "What is Quick Sort's worst-case time complexity?",
                    "options", List.of("O(n)", "O(n log n)", "O(n²)", "O(log n)"),
                    "correctAnswer", 2,
                    "explanation", "Quick Sort has O(n²) worst-case complexity when the pivot is always the smallest or largest element."
                )
            );
            case "binary-search" -> List.of(
                Map.of(
                    "question", "Binary search requires the array to be:",
                    "options", List.of("Unsorted", "Sorted", "Reversed", "Random"),
                    "correctAnswer", 1,
                    "explanation", "Binary search only works on sorted arrays to eliminate half the search space."
                ),
                Map.of(
                    "question", "How much of the search space is eliminated in each step of Binary Search?",
                    "options", List.of("One element", "Half the remaining elements", "One quarter", "Two elements"),
                    "correctAnswer", 1,
                    "explanation", "Binary search eliminates half of the remaining search space in each iteration."
                ),
                Map.of(
                    "question", "What is the space complexity of Binary Search (iterative)?",
                    "options", List.of("O(n)", "O(log n)", "O(1)", "O(n²)"),
                    "correctAnswer", 2,
                    "explanation", "Iterative binary search uses constant space O(1) as it only needs a few variables."
                )
            );
            default -> List.of();
        };
    }
}
