package com.algoviz.codeexecution.dto;

import com.algoviz.codeexecution.entity.Problem;
import lombok.Data;
import java.util.List;

@Data
public class ProblemDTO {
    
    private Long id;
    private String title;
    private String description;
    private String scenario;
    private String inputFormat;
    private String outputFormat;
    private String hints;
    private Problem.Difficulty difficulty;
    private String category;
    private Integer timeLimit;
    private Integer memoryLimit;
    private Integer maxScore;
    private Integer xpReward;
    private List<TestCaseDTO> testCases;
    private List<StarterCodeDTO> starterCodes;
}
