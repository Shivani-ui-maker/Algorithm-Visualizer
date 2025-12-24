package com.algo.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "quiz_questions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuizQuestion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quiz_id", nullable = false)
    private Quiz quiz;

    @NotBlank
    @Column(name = "question_text", columnDefinition = "TEXT", nullable = false)
    private String questionText;

    @Column(name = "options_json", columnDefinition = "JSON", nullable = false)
    private String optionsJson;

    @Column(name = "correct_option_index", nullable = false)
    private Integer correctOptionIndex;

    @Column(name = "explanation_text", columnDefinition = "TEXT")
    private String explanationText;

    @Column(name = "order_index", nullable = false)
    private Integer orderIndex = 0;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    
    // Get correct answer based on correct option index
    public String getCorrectAnswer() {
        return correctOptionIndex != null ? correctOptionIndex.toString() : "0";
    }
}
