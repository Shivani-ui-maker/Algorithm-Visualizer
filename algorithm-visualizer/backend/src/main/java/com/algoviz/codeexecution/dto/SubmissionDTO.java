package com.algoviz.codeexecution.dto;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class SubmissionDTO {
    
    private Long id;
    private Long userId;
    private Long problemId;
    private String language;
    private Verdict verdict;
    private Integer score;
    private Long executionTime;
    private Long memoryUsed;
    private Integer passedTests;
    private Integer totalTests;
    private LocalDateTime submittedAt;
}
