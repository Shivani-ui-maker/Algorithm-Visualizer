package com.algo.backend.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "code_submissions")
public class CodeSubmission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "algorithm_id", nullable = false)
    private Algorithm algorithm;

    @Column(name = "source_code", columnDefinition = "TEXT")
    private String sourceCode;

    @Enumerated(EnumType.STRING)
    private Language language;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(name = "execution_time")
    private Double executionTime;

    @Column(name = "memory_usage")
    private Integer memoryUsage;

    @Column(name = "test_cases_passed")
    private Integer testCasesPassed;

    @Column(name = "total_test_cases")
    private Integer totalTestCases;

    @Column(name = "error_message", columnDefinition = "TEXT")
    private String errorMessage;

    @Column(name = "judge0_token")
    private String judge0Token;

    @CreationTimestamp
    @Column(name = "submitted_at", updatable = false)
    private LocalDateTime submittedAt;

    // Constructors
    public CodeSubmission() {}

    public CodeSubmission(User user, Algorithm algorithm, String sourceCode, Language language) {
        this.user = user;
        this.algorithm = algorithm;
        this.sourceCode = sourceCode;
        this.language = language;
        this.status = Status.PENDING;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Algorithm getAlgorithm() { return algorithm; }
    public void setAlgorithm(Algorithm algorithm) { this.algorithm = algorithm; }

    public String getSourceCode() { return sourceCode; }
    public void setSourceCode(String sourceCode) { this.sourceCode = sourceCode; }

    public Language getLanguage() { return language; }
    public void setLanguage(Language language) { this.language = language; }

    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }

    public Double getExecutionTime() { return executionTime; }
    public void setExecutionTime(Double executionTime) { this.executionTime = executionTime; }

    public Integer getMemoryUsage() { return memoryUsage; }
    public void setMemoryUsage(Integer memoryUsage) { this.memoryUsage = memoryUsage; }

    public Integer getTestCasesPassed() { return testCasesPassed; }
    public void setTestCasesPassed(Integer testCasesPassed) { this.testCasesPassed = testCasesPassed; }

    public Integer getTotalTestCases() { return totalTestCases; }
    public void setTotalTestCases(Integer totalTestCases) { this.totalTestCases = totalTestCases; }

    public String getErrorMessage() { return errorMessage; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }

    public String getJudge0Token() { return judge0Token; }
    public void setJudge0Token(String judge0Token) { this.judge0Token = judge0Token; }

    public LocalDateTime getSubmittedAt() { return submittedAt; }
    public void setSubmittedAt(LocalDateTime submittedAt) { this.submittedAt = submittedAt; }

    @Column(columnDefinition = "TEXT")
private String output;

public String getOutput() {
    return output;
}

public void setOutput(String output) {
    this.output = output;
}

    public enum Language {
        JAVA, PYTHON, CPP, JAVASCRIPT
    }

    public enum Status {
        PENDING, ACCEPTED, WRONG_ANSWER, TIME_LIMIT_EXCEEDED, 
        MEMORY_LIMIT_EXCEEDED, RUNTIME_ERROR, COMPILATION_ERROR
    }
}
