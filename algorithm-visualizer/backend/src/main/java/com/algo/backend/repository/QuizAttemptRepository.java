package com.algo.backend.repository;

import com.algo.backend.entity.QuizAttempt;
import com.algo.backend.entity.User;
import com.algo.backend.entity.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface QuizAttemptRepository extends JpaRepository<QuizAttempt, Long> {
    
    List<QuizAttempt> findByUser(User user);
    
    List<QuizAttempt> findByQuiz(Quiz quiz);
    
    List<QuizAttempt> findByUserAndQuiz(User user, Quiz quiz);
    
    @Query("SELECT qa FROM QuizAttempt qa WHERE qa.user.id = :userId ORDER BY qa.attemptedAt DESC")
    List<QuizAttempt> findByUserIdOrderByAttemptedAtDesc(@Param("userId") Long userId);
    
    @Query("SELECT qa FROM QuizAttempt qa WHERE qa.user.id = :userId AND qa.isCorrect = true")
    List<QuizAttempt> findCorrectAttemptsByUser(@Param("userId") Long userId);
    
    @Query("SELECT COUNT(qa) FROM QuizAttempt qa WHERE qa.user.id = :userId AND qa.isCorrect = true")
    Long countCorrectAttemptsByUser(@Param("userId") Long userId);
    
    @Query("SELECT AVG(qa.scoreEarned) FROM QuizAttempt qa WHERE qa.user.id = :userId")
    Double getAverageScoreByUser(@Param("userId") Long userId);
    
    @Query("SELECT qa FROM QuizAttempt qa WHERE qa.attemptedAt BETWEEN :startDate AND :endDate")
    List<QuizAttempt> findAttemptsBetweenDates(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
}
