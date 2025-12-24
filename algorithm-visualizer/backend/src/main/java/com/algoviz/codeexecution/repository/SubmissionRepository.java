package com.algoviz.codeexecution.repository;

import com.algoviz.codeexecution.entity.Submission;
import com.algoviz.codeexecution.dto.Verdict;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SubmissionRepository extends JpaRepository<Submission, Long> {
    
    List<Submission> findByUserIdAndProblemIdOrderBySubmittedAtDesc(Long userId, Long problemId);
    
    List<Submission> findByUserIdOrderBySubmittedAtDesc(Long userId);
    
    List<Submission> findByProblemIdOrderBySubmittedAtDesc(Long problemId);
    
    @Query("SELECT s FROM Submission s WHERE s.userId = :userId AND s.verdict = :verdict")
    List<Submission> findByUserIdAndVerdict(Long userId, Verdict verdict);
    
    @Query("SELECT COUNT(DISTINCT s.problemId) FROM Submission s WHERE s.userId = :userId AND s.verdict = 'ACCEPTED'")
    Long countSolvedProblemsByUserId(Long userId);
    
    @Query("SELECT s FROM Submission s WHERE s.submittedAt BETWEEN :startDate AND :endDate ORDER BY s.submittedAt DESC")
    List<Submission> findBySubmittedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    @Query("SELECT s FROM Submission s WHERE s.userId = :userId AND s.problemId = :problemId AND s.verdict = 'ACCEPTED' ORDER BY s.submittedAt ASC LIMIT 1")
    Submission findFirstAcceptedSubmission(Long userId, Long problemId);
}
