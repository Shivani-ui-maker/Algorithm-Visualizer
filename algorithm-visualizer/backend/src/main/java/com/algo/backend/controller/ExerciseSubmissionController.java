package com.algo.backend.controller;

import com.algo.backend.entity.ExerciseSubmission;
import com.algo.backend.service.ExerciseSubmissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/exercise-submissions")
public class ExerciseSubmissionController {

    @Autowired
    private ExerciseSubmissionService submissionService;

    // Get all submissions by user
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ExerciseSubmission>> getByUser(@PathVariable String userId) {
        return ResponseEntity.ok(submissionService.getSubmissionsByUser(userId));
    }

    // Get passed submissions by user
    @GetMapping("/user/{userId}/passed")
    public ResponseEntity<List<ExerciseSubmission>> getPassedByUser(@PathVariable String userId) {
        return ResponseEntity.ok(submissionService.getPassedSubmissions(userId));
    }

    // Count passed submissions by user
    @GetMapping("/user/{userId}/passed/count")
    public ResponseEntity<Long> countPassedByUser(@PathVariable String userId) {
        return ResponseEntity.ok(submissionService.countPassedByUser(userId));
    }

    // Get submissions by exercise
    @GetMapping("/exercise/{exerciseId}")
    public ResponseEntity<List<ExerciseSubmission>> getByExercise(@PathVariable Long exerciseId) {
        return ResponseEntity.ok(submissionService.getByExercise(exerciseId));
    }
}

