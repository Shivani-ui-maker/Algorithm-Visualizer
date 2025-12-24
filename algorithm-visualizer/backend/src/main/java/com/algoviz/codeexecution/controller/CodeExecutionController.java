package com.algoviz.codeexecution.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.algoviz.codeexecution.dto.CodeEvaluationRequestDTO;
import com.algoviz.codeexecution.dto.CodeExecutionRequestDTO;
import com.algoviz.codeexecution.dto.EvaluationResultDTO;
import com.algoviz.codeexecution.dto.ExecutionResultDTO;
import com.algoviz.codeexecution.dto.ProblemDTO;
import com.algoviz.codeexecution.dto.SubmissionDTO;
import com.algoviz.codeexecution.dto.Verdict;
import com.algoviz.codeexecution.service.EnhancedCodeExecutionService;
import com.algoviz.codeexecution.service.ProblemService;

@RestController
@RequestMapping("/api/code")
@CrossOrigin(origins = "*")
public class CodeExecutionController {

    @Autowired
    private EnhancedCodeExecutionService codeExecutionService;
    
    @Autowired
    private ProblemService problemService;

    /**
     * Execute code without evaluation - for Run Code functionality
     */
    @PostMapping("/run")
    public ResponseEntity<ExecutionResultDTO> runCode(@RequestBody CodeExecutionRequestDTO request) {
        try {
            // Validate request
            if (request.getCode() == null || request.getCode().trim().isEmpty()) {
                return ResponseEntity.ok(ExecutionResultDTO.builder()
                    .success(false)
                    .error("⚠️ No implementation found, please write your code first.")
                    .executionTime(0L)
                    .exitCode(1)
                    .build());
            }
            
            ExecutionResultDTO result = codeExecutionService.executeCode(request);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            ExecutionResultDTO errorResult = ExecutionResultDTO.builder()
                .success(false)
                .error("🧯 Runtime Error: " + e.getMessage())
                .executionTime(0L)
                .exitCode(1)
                .build();
            return ResponseEntity.ok(errorResult);
        }
    }

    /**
     * Evaluate code against problem test cases - for Submit functionality
     */
    @PostMapping("/evaluate")
    public ResponseEntity<EvaluationResultDTO> evaluateCode(@RequestBody CodeEvaluationRequestDTO request) {
        try {
            // Validate request
            if (request.getCode() == null || request.getCode().trim().isEmpty()) {
                return ResponseEntity.ok(EvaluationResultDTO.builder()
                    .verdict(Verdict.COMPILATION_ERROR)
                    .score(0)
                    .message("⚠️ No implementation found, please write your code first.")
                    .executionTime(0L)
                    .totalTestCases(0)
                    .passedTestCases(0)
                    .build());
            }
            
            EvaluationResultDTO result = codeExecutionService.evaluateCode(request);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            EvaluationResultDTO errorResult = EvaluationResultDTO.builder()
                .verdict(Verdict.INTERNAL_ERROR)
                .score(0)
                .message("🧯 Runtime Error: " + e.getMessage())
                .executionTime(0L)
                .totalTestCases(0)
                .passedTestCases(0)
                .build();
            return ResponseEntity.ok(errorResult);
        }
    }
    
    /**
     * Get all problems
     */
    @GetMapping("/problems")
    public ResponseEntity<List<ProblemDTO>> getAllProblems() {
        try {
            List<ProblemDTO> problems = problemService.getAllProblems();
            return ResponseEntity.ok(problems);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    /**
     * Get specific problem by ID
     */
    @GetMapping("/problems/{id}")
    public ResponseEntity<ProblemDTO> getProblem(@PathVariable Long id) {
        try {
            ProblemDTO problem = problemService.getProblemById(id);
            return ResponseEntity.ok(problem);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Get user's submission history for a problem
     */
    @GetMapping("/submissions/{userId}/{problemId}")
    public ResponseEntity<List<SubmissionDTO>> getUserSubmissions(
            @PathVariable Long userId,
            @PathVariable Long problemId) {
        try {
            // Implementation would fetch from submission repository
            return ResponseEntity.ok(List.of());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    /**
     * Health check endpoint
     */
    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Code Execution Service is running! 🚀");
    }
}
