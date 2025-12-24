package com.algo.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "exercise_submissions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExerciseSubmission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exercise_id", nullable = false)
    private Exercise exercise;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "guest_session_id")
    private String guestSessionId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Language language;

    @NotBlank
    @Column(columnDefinition = "TEXT", nullable = false)
    private String code;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status = Status.PENDING;

    @Column(name = "test_results", columnDefinition = "JSON")
    private String testResults;

    @Column(name = "execution_time_ms")
    private Integer executionTimeMs;

    @Column(name = "memory_usage_kb")
    private Integer memoryUsageKb;

    @Column(name = "passed")
    private boolean passed = false;

    @CreationTimestamp
    @Column(name = "submitted_at")
    private LocalDateTime submittedAt;

    public enum Language {
        python, java, cpp
    }

    public enum Status {
        PENDING, RUNNING, PASSED, FAILED, ERROR
    }
}
