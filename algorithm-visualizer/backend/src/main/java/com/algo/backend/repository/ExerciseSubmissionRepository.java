package com.algo.backend.repository;

import com.algo.backend.entity.ExerciseSubmission;
import com.algo.backend.entity.Exercise;
import com.algo.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExerciseSubmissionRepository extends JpaRepository<ExerciseSubmission, Long> {
    
    List<ExerciseSubmission> findByExercise(Exercise exercise);
    
    List<ExerciseSubmission> findByUser(User user);
    
    List<ExerciseSubmission> findByUserAndExercise(User user, Exercise exercise);
    
    List<ExerciseSubmission> findByGuestSessionId(String guestSessionId);
    
    // Methods for guest session support
    List<ExerciseSubmission> findByExerciseId(Long exerciseId);
    
    List<ExerciseSubmission> findByExerciseIdAndGuestSessionId(Long exerciseId, String guestSessionId);
    
    Long countByExerciseId(Long exerciseId);
    
    Long countByExerciseIdAndPassed(Long exerciseId, boolean passed);
    
    @Query("SELECT AVG(subquery.attemptCount) FROM (SELECT COUNT(es) as attemptCount FROM ExerciseSubmission es WHERE es.exercise.id = :exerciseId GROUP BY COALESCE(es.user.id, es.guestSessionId)) subquery")
    Double getAverageAttemptsByExerciseId(@Param("exerciseId") Long exerciseId);
    
    @Query("SELECT COUNT(DISTINCT es.exercise.id) FROM ExerciseSubmission es WHERE es.user.id = :userId AND es.passed = :passed")
    Long countDistinctExercisesByUserIdAndPassed(@Param("userId") Long userId, @Param("passed") boolean passed);
    
    @Query("SELECT COALESCE(COUNT(es), 0) FROM ExerciseSubmission es WHERE es.user.id = :userId AND es.passed = true AND es.submittedAt >= COALESCE((SELECT MAX(es2.submittedAt) FROM ExerciseSubmission es2 WHERE es2.user.id = :userId AND es2.passed = false), '1900-01-01')")
    Long getCurrentStreakByUserId(@Param("userId") Long userId);
    
    @Query("SELECT es FROM ExerciseSubmission es WHERE es.user.id = :userId AND es.status = 'PASSED'")
    List<ExerciseSubmission> findPassedSubmissionsByUser(@Param("userId") Long userId);
    
    @Query("SELECT es FROM ExerciseSubmission es WHERE es.exercise.id = :exerciseId AND es.status = 'PASSED' ORDER BY es.submittedAt DESC")
    List<ExerciseSubmission> findPassedSubmissionsByExercise(@Param("exerciseId") Long exerciseId);
    
    Optional<ExerciseSubmission> findTopByUserAndExerciseAndStatusOrderBySubmittedAtDesc(
        User user, Exercise exercise, ExerciseSubmission.Status status);
}
