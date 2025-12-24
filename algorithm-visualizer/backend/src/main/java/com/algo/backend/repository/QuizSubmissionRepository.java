package com.algo.backend.repository;

import com.algo.backend.entity.QuizSubmission;
import com.algo.backend.entity.Quiz;
import com.algo.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QuizSubmissionRepository extends JpaRepository<QuizSubmission, Long> {
    
    List<QuizSubmission> findByQuiz(Quiz quiz);
    
    List<QuizSubmission> findByUser(User user);
    
    List<QuizSubmission> findByUserAndQuiz(User user, Quiz quiz);
    
    List<QuizSubmission> findByGuestSessionId(String guestSessionId);
    
    @Query("SELECT qs FROM QuizSubmission qs WHERE qs.user.id = :userId ORDER BY qs.submittedAt DESC")
    List<QuizSubmission> findByUserOrderBySubmittedAtDesc(@Param("userId") Long userId);
    
    @Query("SELECT AVG(qs.score) FROM QuizSubmission qs WHERE qs.user.id = :userId")
    Double getAverageScoreByUser(@Param("userId") Long userId);
    
    @Query("SELECT SUM(qs.score) FROM QuizSubmission qs WHERE qs.user.id = :userId")
    Long getTotalScoreByUser(@Param("userId") Long userId);
    
    Optional<QuizSubmission> findTopByUserAndQuizOrderByScoreDescSubmittedAtDesc(User user, Quiz quiz);
}
