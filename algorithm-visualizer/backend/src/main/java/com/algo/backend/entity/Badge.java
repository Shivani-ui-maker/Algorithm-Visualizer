package com.algo.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "badges")
public class Badge {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(unique = true)
    private String name;

    @NotBlank
    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "icon_url")
    private String iconUrl;

    @Enumerated(EnumType.STRING)
    private BadgeType type;

    @Column(name = "required_value")
    private Integer requiredValue;

    @Column(name = "is_active", columnDefinition = "boolean default true")
    private Boolean isActive = true;

    @OneToMany(mappedBy = "badge", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<UserBadge> userBadges;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    // Constructors
    public Badge() {}

    public Badge(String name, String description, BadgeType type, Integer requiredValue) {
        this.name = name;
        this.description = description;
        this.type = type;
        this.requiredValue = requiredValue;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getIconUrl() { return iconUrl; }
    public void setIconUrl(String iconUrl) { this.iconUrl = iconUrl; }

    public BadgeType getType() { return type; }
    public void setType(BadgeType type) { this.type = type; }

    public Integer getRequiredValue() { return requiredValue; }
    public void setRequiredValue(Integer requiredValue) { this.requiredValue = requiredValue; }

    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }

    public List<UserBadge> getUserBadges() { return userBadges; }
    public void setUserBadges(List<UserBadge> userBadges) { this.userBadges = userBadges; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public enum BadgeType {
        ALGORITHMS_COMPLETED, QUIZ_SCORE, STREAK_DAYS, 
        FIRST_SUBMISSION, PERFECT_SCORE, SPEED_DEMON
    }
}
