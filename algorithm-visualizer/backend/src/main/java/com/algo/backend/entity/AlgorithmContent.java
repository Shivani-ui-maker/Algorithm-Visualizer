package com.algo.backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "algorithm_content")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlgorithmContent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "algorithm_id", nullable = false)
    private Algorithm algorithm;

    @Column(name = "time_complexity", length = 100)
    private String timeComplexity;

    @Column(name = "space_complexity", length = 100)
    private String spaceComplexity;

    @Column(name = "real_life_example", columnDefinition = "TEXT")
    private String realLifeExample;

    @Column(columnDefinition = "TEXT")
    private String pseudocode;

    @Column(name = "code_py", columnDefinition = "TEXT")
    private String codePy;

    @Column(name = "code_java", columnDefinition = "TEXT")
    private String codeJava;

    @Column(name = "code_cpp", columnDefinition = "TEXT")
    private String codeCpp;

    @Column(name = "visualization_steps", columnDefinition = "JSON")
    private String visualizationSteps;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
