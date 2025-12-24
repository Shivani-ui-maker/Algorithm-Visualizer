package com.algoviz.codeexecution.repository;

import com.algoviz.codeexecution.entity.Problem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProblemRepository extends JpaRepository<Problem, Long> {
    
    List<Problem> findByDifficulty(Problem.Difficulty difficulty);
    
    List<Problem> findByCategory(String category);
    
    @Query("SELECT p FROM Problem p LEFT JOIN FETCH p.testCases WHERE p.id = :id")
    Optional<Problem> findByIdWithTestCases(Long id);
    
    @Query("SELECT p FROM Problem p LEFT JOIN FETCH p.starterCodes WHERE p.id = :id")
    Optional<Problem> findByIdWithStarterCodes(Long id);
    
    @Query("SELECT p FROM Problem p LEFT JOIN FETCH p.testCases LEFT JOIN FETCH p.starterCodes WHERE p.id = :id")
    Optional<Problem> findByIdWithAll(Long id);
}
