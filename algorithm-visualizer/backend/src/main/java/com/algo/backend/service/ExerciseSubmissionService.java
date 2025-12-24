package com.algo.backend.service;

import com.algo.backend.entity.ExerciseSubmission;
import com.algo.backend.repository.ExerciseSubmissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExerciseSubmissionService {

    @Autowired
    private ExerciseSubmissionRepository submissionRepository;


    // Save submission
    public ExerciseSubmission saveSubmission(ExerciseSubmission submission) {
        return submissionRepository.save(submission);
    }

    // Get submissions by guest session
    public List<ExerciseSubmission> getSubmissionsByUser(String userId) {
        return submissionRepository.findByGuestSessionId(userId);
    }

    // Get passed submissions by guest session
    public List<ExerciseSubmission> getPassedSubmissions(String userId) {
        // For now, filter manually since we don't have the exact method
        return submissionRepository.findByGuestSessionId(userId).stream()
                .filter(ExerciseSubmission::isPassed)
                .toList();
    }

    // Count passed submissions by guest session
    public Long countPassedByUser(String userId) {
        return (long) submissionRepository.findByGuestSessionId(userId).stream()
                .mapToInt(s -> s.isPassed() ? 1 : 0)
                .sum();
    }

    // Get submissions by exercise
    public List<ExerciseSubmission> getByExercise(Long exerciseId) {
        return submissionRepository.findByExerciseId(exerciseId);
    }
}

