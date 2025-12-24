package com.algoviz.codeexecution.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CodeExecutionRequestDTO {
    
    @NotBlank(message = "Code cannot be empty")
    private String code;
    
    @NotNull(message = "Language is required")
    private Language language;
    
    private String input; // Optional input for testing
}
