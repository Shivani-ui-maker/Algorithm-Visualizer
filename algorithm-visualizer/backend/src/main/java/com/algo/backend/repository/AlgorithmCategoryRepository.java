package com.algo.backend.repository;

import com.algo.backend.entity.AlgorithmCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AlgorithmCategoryRepository extends JpaRepository<AlgorithmCategory, Long> {
    
    Optional<AlgorithmCategory> findByName(String name);
    
    Optional<AlgorithmCategory> findBySlug(String slug);
    
    @Query("SELECT ac FROM AlgorithmCategory ac ORDER BY ac.orderIndex")
    List<AlgorithmCategory> findAllOrderedByIndex();
    
    List<AlgorithmCategory> findByOrderByOrderIndex();
}
