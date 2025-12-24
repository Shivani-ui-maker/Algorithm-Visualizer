package com.algo.backend.repository;

import com.algo.backend.entity.Exercise;
import com.algo.backend.entity.Algorithm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExerciseRepository extends JpaRepository<Exercise, Long> {
    
    List<Exercise> findByAlgorithm(Algorithm algorithm);
    
    List<Exercise> findByAlgorithmId(Long algorithmId);
    
    List<Exercise> findByDifficulty(Exercise.Difficulty difficulty);
    
    @Query("SELECT e FROM Exercise e WHERE e.algorithm.id = :algorithmId ORDER BY e.levelNumber")
    List<Exercise> findByAlgorithmOrderByLevel(@Param("algorithmId") Long algorithmId);
    
    @Query("SELECT e FROM Exercise e ORDER BY RAND() LIMIT :limit")
    List<Exercise> findRandomExercises(@Param("limit") int limit);
    
    @Query("SELECT e FROM Exercise e WHERE e.algorithm.category.name = :categoryName")
    List<Exercise> findByAlgorithmCategoryName(@Param("categoryName") String categoryName);
}
