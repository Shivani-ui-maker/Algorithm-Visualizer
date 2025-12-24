package com.algoviz.codeexecution.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CodeEvaluationRequestDTO {
    
    @NotBlank(message = "Code cannot be empty")
    private String code;
    
    @NotNull(message = "Language is required")
    private Language language;
    
    @NotNull(message = "Problem ID is required")
    private Long problemId;
    
    @NotNull(message = "User ID is required")
    private Long userId;
}
