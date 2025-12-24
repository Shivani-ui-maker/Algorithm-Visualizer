package com.algoviz.codeexecution.dto;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class EvaluationResultDTO {
    
    private Verdict verdict;
    private int score;
    private String message;
    private List<TestCaseResultDTO> testResults;
    private long executionTime;
    private long memoryUsed;
    private int totalTestCases;
    private int passedTestCases;
}
