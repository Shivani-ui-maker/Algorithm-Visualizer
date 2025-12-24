package com.algoviz.codeexecution.dto;

import lombok.Data;

@Data
public class TestCaseDTO {
    
    private Long id;
    private Integer testNumber;
    private String input;
    private String expectedOutput;
    private Boolean isSample;
    private String explanation;
    private Integer timeLimit;
    private Integer memoryLimit;
}
