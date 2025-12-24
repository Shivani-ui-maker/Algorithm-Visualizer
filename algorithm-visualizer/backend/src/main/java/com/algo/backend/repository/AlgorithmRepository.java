package com.algo.backend.repository;

import com.algo.backend.entity.Algorithm;
import com.algo.backend.entity.AlgorithmCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AlgorithmRepository extends JpaRepository<Algorithm, Long> {
    
    Optional<Algorithm> findByName(String name);
    
    Optional<Algorithm> findBySlug(String slug);
    
    List<Algorithm> findByCategory(AlgorithmCategory category);
    
    List<Algorithm> findByDifficulty(Algorithm.Difficulty difficulty);
    
    List<Algorithm> findByCategoryAndDifficulty(AlgorithmCategory category, Algorithm.Difficulty difficulty);
    
    @Query("SELECT a FROM Algorithm a ORDER BY a.category.orderIndex, a.name")
    List<Algorithm> findAllOrderedByCategory();
    
    @Query("SELECT a FROM Algorithm a WHERE a.category = :category ORDER BY a.name")
    List<Algorithm> findByCategoryOrderByName(@Param("category") AlgorithmCategory category);
    
    @Query(value = "SELECT * FROM algorithms ORDER BY RAND() LIMIT :limit", nativeQuery = true)
    List<Algorithm> findRandomAlgorithms(@Param("limit") int limit);
}
