package com.algo.backend.repository;

import com.algo.backend.entity.Quiz;
import com.algo.backend.entity.Algorithm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuizRepository extends JpaRepository<Quiz, Long> {
    
    List<Quiz> findByAlgorithm(Algorithm algorithm);
    
    List<Quiz> findByAlgorithmId(Long algorithmId);
    
    List<Quiz> findByDifficulty(Quiz.Difficulty difficulty);
    
    @Query("SELECT q FROM Quiz q WHERE q.algorithm.id = :algorithmId ORDER BY RAND() LIMIT :limit")
    List<Quiz> findRandomQuizzesByAlgorithm(@Param("algorithmId") Long algorithmId, @Param("limit") int limit);
    
    @Query("SELECT q FROM Quiz q ORDER BY RAND() LIMIT :limit")
    List<Quiz> findRandomQuizzes(@Param("limit") int limit);
    
    @Query("SELECT q FROM Quiz q WHERE q.difficulty = :difficulty ORDER BY RAND() LIMIT :limit")
    List<Quiz> findRandomQuizzesByDifficulty(@Param("difficulty") Quiz.Difficulty difficulty, @Param("limit") int limit);
}
