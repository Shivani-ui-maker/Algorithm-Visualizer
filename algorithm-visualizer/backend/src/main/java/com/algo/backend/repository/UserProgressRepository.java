package com.algo.backend.repository;

import com.algo.backend.entity.UserProgress;
import com.algo.backend.entity.User;
import com.algo.backend.entity.Algorithm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserProgressRepository extends JpaRepository<UserProgress, Long> {
    
    List<UserProgress> findByUser(User user);
    
    List<UserProgress> findByAlgorithm(Algorithm algorithm);
    
    Optional<UserProgress> findByUserAndAlgorithm(User user, Algorithm algorithm);
    
    @Query("SELECT up FROM UserProgress up WHERE up.user.id = :userId AND up.status = 'COMPLETED'")
    List<UserProgress> findCompletedProgressByUser(@Param("userId") Long userId);
    
    @Query("SELECT COUNT(up) FROM UserProgress up WHERE up.user.id = :userId AND up.status = 'COMPLETED'")
    Long countCompletedAlgorithmsByUser(@Param("userId") Long userId);
    
    @Query("SELECT AVG(up.bestScore) FROM UserProgress up WHERE up.user.id = :userId AND up.bestScore IS NOT NULL")
    Double getAverageBestScoreByUser(@Param("userId") Long userId);
    
    @Query("SELECT SUM(up.timeSpentSeconds) FROM UserProgress up WHERE up.user.id = :userId")
    Long getTotalTimeSpentByUser(@Param("userId") Long userId);
    
    List<UserProgress> findByStatus(UserProgress.Status status);
    
    Long countByUserAndStatus(User user, UserProgress.Status status);
    
    Long countByUser(User user);
}
