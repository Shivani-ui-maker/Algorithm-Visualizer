package com.algoviz.codeexecution.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ExecutionResultDTO {
    
    private boolean success;
    private String output;
    private String error;
    private String compilerOutput;
    private long executionTime; // in milliseconds
    private long memoryUsed; // in bytes
    private int exitCode;
}
