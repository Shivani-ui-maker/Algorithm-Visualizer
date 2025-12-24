package com.algo.backend.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_activities")
public class UserActivity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String activityType;

    @Column(name = "activity_details", columnDefinition = "TEXT")
    private String activityDetails;

    @Column(name = "points_earned")
    private Integer pointsEarned = 0;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "metadata", columnDefinition = "JSON")
    private String metadata;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public String getActivityType() { return activityType; }
    public void setActivityType(String activityType) { this.activityType = activityType; }

    public String getActivityDetails() { return activityDetails; }
    public void setActivityDetails(String activityDetails) { this.activityDetails = activityDetails; }

    public Integer getPointsEarned() { return pointsEarned; }
    public void setPointsEarned(Integer pointsEarned) { this.pointsEarned = pointsEarned; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public String getMetadata() { return metadata; }
    public void setMetadata(String metadata) { this.metadata = metadata; }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    // Activity types as constants
    public static final class ActivityType {
        public static final String LOGIN = "LOGIN";
        public static final String ALGORITHM_COMPLETED = "ALGORITHM_COMPLETED";
        public static final String QUIZ_ATTEMPT = "QUIZ_ATTEMPT";
        public static final String CODE_SUBMISSION = "CODE_SUBMISSION";
        public static final String STREAK_ACHIEVEMENT = "STREAK_ACHIEVEMENT";
        public static final String BADGE_EARNED = "BADGE_EARNED";
        
        private ActivityType() {
            // Private constructor to prevent instantiation
        }
    }
}
