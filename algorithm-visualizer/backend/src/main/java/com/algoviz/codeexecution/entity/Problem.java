package com.algoviz.codeexecution.entity;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "problems")
@Data
public class Problem {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String title;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @Column(columnDefinition = "TEXT")
    private String scenario;
    
    @Column(name = "input_format", columnDefinition = "TEXT")
    private String inputFormat;
    
    @Column(name = "output_format", columnDefinition = "TEXT")
    private String outputFormat;
    
    @Column(columnDefinition = "TEXT")
    private String hints;
    
    @Enumerated(EnumType.STRING)
    private Difficulty difficulty;
    
    private String category;
    
    @Column(name = "time_limit")
    private Integer timeLimit = 2000; // milliseconds
    
    @Column(name = "memory_limit")
    private Integer memoryLimit = 128; // MB
    
    @Column(name = "max_score")
    private Integer maxScore = 100;
    
    @Column(name = "xp_reward")
    private Integer xpReward = 50;
    
    @OneToMany(mappedBy = "problem", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<TestCase> testCases;
    
    @OneToMany(mappedBy = "problem", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<StarterCode> starterCodes;
    
    public enum Difficulty {
        EASY, MEDIUM, HARD
    }
}
