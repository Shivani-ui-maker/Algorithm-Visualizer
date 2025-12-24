package com.algo.backend.repository;

import com.algo.backend.entity.Badge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BadgeRepository extends JpaRepository<Badge, Long> {
    
    List<Badge> findByType(Badge.BadgeType type);
    
    @Query("SELECT b FROM Badge b WHERE b.isActive = true")
    List<Badge> findAllActive();
    
    @Query("SELECT b FROM Badge b WHERE b.isActive = true ORDER BY b.type, b.requiredValue")
    List<Badge> findAllActiveOrderByTypeAndValue();
    
    List<Badge> findByIsActiveTrue();
}
