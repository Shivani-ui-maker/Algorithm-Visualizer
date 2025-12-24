package com.algoviz.codeexecution.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TestCaseResultDTO {
    
    private int testCaseNumber;
    private String input;
    private String expectedOutput;
    private String actualOutput;
    private boolean passed;
    private long executionTime;
    private String error;
    private Verdict verdict;
}
