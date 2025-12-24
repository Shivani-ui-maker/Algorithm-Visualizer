package com.algo.backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_progress", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"user_id", "algorithm_id"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserProgress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "algorithm_id", nullable = false)
    private Algorithm algorithm;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status = Status.VIEWED;

    @Column(name = "best_score")
    private Integer bestScore = 0;

    @Column(nullable = false)
    private Integer attempts = 0;

    @Column(name = "time_spent_seconds")
    private Integer timeSpentSeconds = 0;

    @Column(name = "steps_completed")
    private Integer stepsCompleted = 0;

    @Column(name = "total_steps")
    private Integer totalSteps = 0;

    @Column(name = "difficulty")
    private String difficulty = "medium";

    @Column(name = "last_viewed_at")
    private LocalDateTime lastViewedAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public enum Status {
        VIEWED, COMPLETED
    }

    // Constructor for backward compatibility
    public UserProgress(User user, Algorithm algorithm) {
        this.user = user;
        this.algorithm = algorithm;
        this.status = Status.VIEWED;
        this.bestScore = 0;
        this.attempts = 0;
        this.timeSpentSeconds = 0;
        this.stepsCompleted = 0;
        this.totalSteps = 0;
        this.difficulty = "medium";
    }

    // Convenience methods
    public boolean getIsCompleted() {
        return this.status == Status.COMPLETED;
    }

    public void setIsCompleted(boolean completed) {
        this.status = completed ? Status.COMPLETED : Status.VIEWED;
        if (completed && this.completedAt == null) {
            this.completedAt = LocalDateTime.now();
        }
    }

    // Explicit getters/setters for bestScore
    public Integer getBestScore() {
        return this.bestScore != null ? this.bestScore : 0;
    }

    public void setBestScore(Integer bestScore) {
        this.bestScore = (bestScore != null ? bestScore : 0);
    }

    // Backward compatibility
    @Deprecated
    public Integer getQuizScore() {
        return this.getBestScore();
    }

    @Deprecated
    public void setQuizScore(Integer score) {
        this.setBestScore(score);
    }

    public Integer getTimeSpent() {
        return this.timeSpentSeconds;
    }

    public void setTimeSpent(Integer timeSpent) {
        this.timeSpentSeconds = timeSpent;
    }

    public Integer getAttemptsCount() {
        return this.attempts;
    }

    public void setAttemptsCount(Integer attempts) {
        this.attempts = attempts;
    }
}
