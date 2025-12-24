package com.algo.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "algorithms")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Algorithm {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private AlgorithmCategory category;

    @NotBlank
    @Column(nullable = false, length = 100)
    private String name;

    @NotBlank
    @Column(nullable = false, unique = true, length = 100)
    private String slug;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AlgorithmType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Difficulty difficulty = Difficulty.MEDIUM;

    @Column(columnDefinition = "TEXT")
    private String description;

    @OneToOne(mappedBy = "algorithm", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private AlgorithmContent content;

    @OneToMany(mappedBy = "algorithm", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Quiz> quizzes;

    @OneToMany(mappedBy = "algorithm", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<UserProgress> userProgress;

    @OneToMany(mappedBy = "algorithm", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Exercise> exercises;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    public enum AlgorithmType {
        ARRAY, LINKED_LIST, STACK, QUEUE, TREE, GRAPH, DP
    }

    public enum Difficulty {
        EASY, MEDIUM, HARD
    }
}
