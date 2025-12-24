package com.algoviz.codeexecution.entity;

import com.algoviz.codeexecution.dto.Verdict;
import lombok.Data;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "submissions")
@Data
public class Submission {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "user_id", nullable = false)
    private Long userId;
    
    @Column(name = "problem_id", nullable = false)
    private Long problemId;
    
    @Column(nullable = false)
    private String language;
    
    @Column(columnDefinition = "TEXT", nullable = false)
    private String code;
    
    @Enumerated(EnumType.STRING)
    private Verdict verdict;
    
    private Integer score;
    
    @Column(name = "execution_time")
    private Long executionTime; // milliseconds
    
    @Column(name = "memory_used")
    private Long memoryUsed; // bytes
    
    @Column(name = "passed_tests")
    private Integer passedTests;
    
    @Column(name = "total_tests")
    private Integer totalTests;
    
    @Column(columnDefinition = "TEXT")
    private String error;
    
    @Column(name = "submitted_at")
    private LocalDateTime submittedAt;
    
    @PrePersist
    protected void onCreate() {
        submittedAt = LocalDateTime.now();
    }
}
