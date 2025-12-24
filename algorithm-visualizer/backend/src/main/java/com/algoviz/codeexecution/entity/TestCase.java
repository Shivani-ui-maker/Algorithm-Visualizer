package com.algoviz.codeexecution.entity;

import lombok.Data;
import jakarta.persistence.*;

@Entity
@Table(name = "test_cases")
@Data
public class TestCase {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "problem_id", nullable = false)
    private Problem problem;
    
    @Column(name = "test_number")
    private Integer testNumber;
    
    @Column(columnDefinition = "TEXT")
    private String input;
    
    @Column(name = "expected_output", columnDefinition = "TEXT")
    private String expectedOutput;
    
    @Column(name = "is_sample")
    private Boolean isSample = false;
    
    private String explanation;
    
    @Column(name = "time_limit")
    private Integer timeLimit; // Override problem time limit if needed
    
    @Column(name = "memory_limit")
    private Integer memoryLimit; // Override problem memory limit if needed
}
