package com.algo.backend.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.algo.backend.entity.Algorithm;
import com.algo.backend.entity.CodeSubmission;
import com.algo.backend.entity.User;

@Repository
public interface CodeSubmissionRepository extends JpaRepository<CodeSubmission, Long> {
    
    List<CodeSubmission> findByUser(User user);
    
    List<CodeSubmission> findByAlgorithm(Algorithm algorithm);
    
    List<CodeSubmission> findByUserAndAlgorithm(User user, Algorithm algorithm);
    
    List<CodeSubmission> findByStatus(CodeSubmission.Status status);
    
    @Query("SELECT cs FROM CodeSubmission cs WHERE cs.user.id = :userId ORDER BY cs.submittedAt DESC")
    List<CodeSubmission> findByUserIdOrderBySubmittedAtDesc(@Param("userId") Long userId);
    
    @Query("SELECT cs FROM CodeSubmission cs WHERE cs.user.id = :userId AND cs.status = 'ACCEPTED'")
    List<CodeSubmission> findAcceptedSubmissionsByUser(@Param("userId") Long userId);
    
    @Query("SELECT COUNT(cs) FROM CodeSubmission cs WHERE cs.user.id = :userId AND cs.status = 'ACCEPTED'")
    Long countAcceptedSubmissionsByUser(@Param("userId") Long userId);

    // ✅ Fixed version using Pageable instead of LIMIT
    @Query("SELECT cs FROM CodeSubmission cs WHERE cs.user.id = :userId AND cs.algorithm.id = :algorithmId AND cs.status = 'ACCEPTED' ORDER BY cs.executionTime ASC")
    List<CodeSubmission> findBestSubmissionsByUserAndAlgorithm(@Param("userId") Long userId,
                                                               @Param("algorithmId") Long algorithmId,
                                                               Pageable pageable);
}
