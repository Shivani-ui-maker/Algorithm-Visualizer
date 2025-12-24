package com.algo.backend.repository;

import com.algo.backend.entity.UserBadge;
import com.algo.backend.entity.User;
import com.algo.backend.entity.Badge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserBadgeRepository extends JpaRepository<UserBadge, Long> {
    
    List<UserBadge> findByUser(User user);
    
    List<UserBadge> findByBadge(Badge badge);
    
    boolean existsByUserAndBadge(User user, Badge badge);
    
    @Query("SELECT ub FROM UserBadge ub WHERE ub.user.id = :userId ORDER BY ub.earnedAt DESC")
    List<UserBadge> findByUserIdOrderByEarnedAtDesc(@Param("userId") Long userId);
    
    @Query("SELECT COUNT(ub) FROM UserBadge ub WHERE ub.user.id = :userId")
    Long countBadgesByUser(@Param("userId") Long userId);
    
    List<UserBadge> findByUserOrderByEarnedAtDesc(User user);
}
