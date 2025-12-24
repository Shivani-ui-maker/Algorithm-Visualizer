package com.algoviz.codeexecution.service;

import com.algoviz.codeexecution.dto.*;
import com.algoviz.codeexecution.entity.Problem;
import com.algoviz.codeexecution.entity.TestCase;
import com.algoviz.codeexecution.entity.Submission;
import com.algoviz.codeexecution.repository.SubmissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class EnhancedCodeExecutionService {

    @Autowired
    private ProblemService problemService;
    
    @Autowired
    private SubmissionRepository submissionRepository;
    
    @Value("${code.execution.temp-dir:/tmp/algoviz}")
    private String tempDir;
    
    @Value("${code.execution.timeout:5000}")
    private long timeoutMs;
    
    @Value("${code.execution.memory-limit:128}")
    private int memoryLimitMB;

    // Language command mappings
    private static final Map<String, String[]> COMPILE_COMMANDS = Map.of(
        "JAVA", new String[]{"javac", "Main.java"},
        "CPP", new String[]{"g++", "Main.cpp", "-o", "Main", "-std=c++17"},
        "PYTHON", new String[]{} // Python doesn't need compilation
    );
    
    private static final Map<String, String[]> RUN_COMMANDS = Map.of(
        "JAVA", new String[]{"java", "Main"},
        "CPP", new String[]{"./Main"},
        "PYTHON", new String[]{"python3", "Main.py"}
    );

    public ExecutionResultDTO executeCode(CodeExecutionRequestDTO request) {
        String sessionId = UUID.randomUUID().toString();
        Path workingDir = null;
        
        try {
            // Create isolated working directory
            workingDir = createWorkingDirectory(sessionId);
            
            // Save code to appropriate file
            String fileName = getFileName(request.getLanguage().name());
            Path codeFile = workingDir.resolve(fileName);
            Files.write(codeFile, request.getCode().getBytes());
            
            // Compile if needed
            CompilationResult compilationResult = compileCode(request.getLanguage().name(), workingDir);
            if (!compilationResult.isSuccess()) {
                return ExecutionResultDTO.builder()
                    .success(false)
                    .error("⚠️ Compilation Error:\n" + compilationResult.getError())
                    .compilerOutput(compilationResult.getError())
                    .executionTime(0L)
                    .exitCode(1)
                    .build();
            }
            
            // Execute code
            ExecutionResultDTO result = runCode(request.getLanguage().name(), workingDir, request.getInput());
            
            return result;
            
        } catch (Exception e) {
            return ExecutionResultDTO.builder()
                .success(false)
                .error("🧯 Runtime Error: " + e.getMessage())
                .executionTime(0L)
                .exitCode(1)
                .build();
        } finally {
            // Cleanup
            if (workingDir != null) {
                cleanupDirectory(workingDir);
            }
        }
    }

    public EvaluationResultDTO evaluateCode(CodeEvaluationRequestDTO request) {
        try {
            // Get problem and test cases
            Problem problem = problemService.getProblemEntityById(request.getProblemId());
            List<TestCase> testCases = problem.getTestCases();
            
            if (testCases.isEmpty()) {
                return EvaluationResultDTO.builder()
                    .verdict(Verdict.INTERNAL_ERROR)
                    .score(0)
                    .message("No test cases found for this problem")
                    .executionTime(0L)
                    .totalTestCases(0)
                    .passedTestCases(0)
                    .testResults(new ArrayList<>())
                    .build();
            }
            
            List<TestCaseResultDTO> testResults = new ArrayList<>();
            int passedTests = 0;
            long totalExecutionTime = 0;
            
            // Run each test case
            for (TestCase testCase : testCases) {
                CodeExecutionRequestDTO execRequest = CodeExecutionRequestDTO.builder()
                    .code(request.getCode())
                    .language(request.getLanguage())
                    .input(testCase.getInput())
                    .build();
                
                ExecutionResultDTO execResult = executeCode(execRequest);
                totalExecutionTime += execResult.getExecutionTime();
                
                TestCaseResultDTO testResult = evaluateTestCase(testCase, execResult);
                testResults.add(testResult);
                
                if (testResult.isPassed()) {
                    passedTests++;
                } else {
                    // Stop on first failure for faster feedback
                    break;
                }
            }
            
            // Determine verdict
            Verdict verdict = determineVerdict(passedTests, testCases.size(), testResults);
            int score = calculateScore(passedTests, testCases.size(), problem.getMaxScore());
            String message = generateMessage(verdict, passedTests, testCases.size());
            
            // Save submission
            saveSubmission(request, verdict, score, passedTests, testCases.size(), totalExecutionTime);
            
            return EvaluationResultDTO.builder()
                .verdict(verdict)
                .score(score)
                .message(message)
                .executionTime(totalExecutionTime)
                .totalTestCases(testCases.size())
                .passedTestCases(passedTests)
                .testResults(testResults)
                .build();
            
        } catch (Exception e) {
            return EvaluationResultDTO.builder()
                .verdict(Verdict.INTERNAL_ERROR)
                .score(0)
                .message("🧯 Evaluation Error: " + e.getMessage())
                .executionTime(0L)
                .totalTestCases(0)
                .passedTestCases(0)
                .testResults(new ArrayList<>())
                .build();
        }
    }
    
    private Path createWorkingDirectory(String sessionId) throws IOException {
        Path baseDir = Paths.get(tempDir);
        if (!Files.exists(baseDir)) {
            Files.createDirectories(baseDir);
        }
        
        Path workingDir = baseDir.resolve("session_" + sessionId);
        Files.createDirectory(workingDir);
        
        // Set restrictive permissions (Unix-like systems)
        try {
            Runtime.getRuntime().exec("chmod 700 " + workingDir.toString());
        } catch (Exception e) {
            // Ignore on Windows
        }
        
        return workingDir;
    }
    
    private String getFileName(String language) {
        return switch (language) {
            case "JAVA" -> "Main.java";
            case "CPP" -> "Main.cpp";
            case "PYTHON" -> "Main.py";
            default -> throw new IllegalArgumentException("Unsupported language: " + language);
        };
    }
    
    private CompilationResult compileCode(String language, Path workingDir) {
        if (language.equals("PYTHON")) {
            return new CompilationResult(true, ""); // Python doesn't need compilation
        }
        
        try {
            String[] command = COMPILE_COMMANDS.get(language);
            ProcessBuilder pb = new ProcessBuilder(command);
            pb.directory(workingDir.toFile());
            pb.redirectErrorStream(true);
            
            Process process = pb.start();
            boolean finished = process.waitFor(10, TimeUnit.SECONDS);
            
            if (!finished) {
                process.destroyForcibly();
                return new CompilationResult(false, "Compilation timeout");
            }
            
            String output = readProcessOutput(process);
            
            if (process.exitValue() != 0) {
                return new CompilationResult(false, output);
            }
            
            return new CompilationResult(true, output);
            
        } catch (Exception e) {
            return new CompilationResult(false, "Compilation failed: " + e.getMessage());
        }
    }
    
    private ExecutionResultDTO runCode(String language, Path workingDir, String input) {
        try {
            String[] command = RUN_COMMANDS.get(language);
            ProcessBuilder pb = new ProcessBuilder(command);
            pb.directory(workingDir.toFile());
            
            // Set resource limits
            pb.environment().put("MALLOC_ARENA_MAX", "1");
            
            long startTime = System.currentTimeMillis();
            Process process = pb.start();
            
            // Provide input
            if (input != null && !input.isEmpty()) {
                try (OutputStreamWriter writer = new OutputStreamWriter(process.getOutputStream())) {
                    writer.write(input);
                    writer.flush();
                }
            }
            
            boolean finished = process.waitFor(timeoutMs, TimeUnit.MILLISECONDS);
            long executionTime = System.currentTimeMillis() - startTime;
            
            if (!finished) {
                process.destroyForcibly();
                return ExecutionResultDTO.builder()
                    .success(false)
                    .error("⏱️ Time Limit Exceeded")
                    .executionTime(executionTime)
                    .exitCode(124) // Timeout exit code
                    .build();
            }
            
            String output = readProcessOutput(process);
            String errorOutput = readProcessError(process);
            
            if (process.exitValue() != 0) {
                return ExecutionResultDTO.builder()
                    .success(false)
                    .error("🧯 Runtime Error:\n" + errorOutput)
                    .executionTime(executionTime)
                    .exitCode(process.exitValue())
                    .build();
            }
            
            return ExecutionResultDTO.builder()
                .success(true)
                .output(output.trim())
                .executionTime(executionTime)
                .exitCode(0)
                .build();
            
        } catch (Exception e) {
            return ExecutionResultDTO.builder()
                .success(false)
                .error("🧯 Runtime Error: " + e.getMessage())
                .executionTime(0L)
                .exitCode(1)
                .build();
        }
    }
    
    private TestCaseResultDTO evaluateTestCase(TestCase testCase, ExecutionResultDTO execResult) {
        TestCaseResultDTO.TestCaseResultDTOBuilder builder = TestCaseResultDTO.builder()
            .testCaseNumber(testCase.getTestNumber())
            .input(testCase.getInput())
            .expectedOutput(testCase.getExpectedOutput())
            .executionTime(execResult.getExecutionTime());
        
        if (!execResult.isSuccess()) {
            return builder
                .actualOutput("")
                .passed(false)
                .error(execResult.getError())
                .verdict(Verdict.RUNTIME_ERROR)
                .build();
        }
        
        String actualOutput = execResult.getOutput().trim();
        String expectedOutput = testCase.getExpectedOutput().trim();
        
        // Normalize whitespace for comparison
        String normalizedActual = normalizeOutput(actualOutput);
        String normalizedExpected = normalizeOutput(expectedOutput);
        
        boolean passed = normalizedActual.equals(normalizedExpected);
        
        return builder
            .actualOutput(actualOutput)
            .passed(passed)
            .verdict(passed ? Verdict.ACCEPTED : Verdict.WRONG_ANSWER)
            .build();
    }
    
    private String normalizeOutput(String output) {
        return output.replaceAll("\\s+", " ").trim();
    }
    
    private Verdict determineVerdict(int passedTests, int totalTests, List<TestCaseResultDTO> testResults) {
        if (passedTests == totalTests) {
            return Verdict.ACCEPTED;
        }
        
        // Check for specific error types in failed tests
        for (TestCaseResultDTO result : testResults) {
            if (!result.isPassed()) {
                return result.getVerdict();
            }
        }
        
        return Verdict.WRONG_ANSWER;
    }
    
    private int calculateScore(int passedTests, int totalTests, int maxScore) {
        if (totalTests == 0) return 0;
        return (int) Math.round((double) passedTests / totalTests * maxScore);
    }
    
    private String generateMessage(Verdict verdict, int passedTests, int totalTests) {
        return switch (verdict) {
            case ACCEPTED -> "🎉 All Tests Passed! Unlock next challenge.";
            case WRONG_ANSWER -> String.format("❌ Test Failed: %d/%d tests passed. Expected output not matched.", passedTests, totalTests);
            case TIME_LIMIT_EXCEEDED -> "⏱️ Time Limit Exceeded";
            case MEMORY_LIMIT_EXCEEDED -> "💾 Memory Limit Exceeded";
            case RUNTIME_ERROR -> "🧯 Runtime Error occurred during execution";
            case COMPILATION_ERROR -> "⚠️ Compilation Error";
            default -> "Unknown error occurred";
        };
    }
    
    private void saveSubmission(CodeEvaluationRequestDTO request, Verdict verdict, int score, 
                               int passedTests, int totalTests, long executionTime) {
        try {
            Submission submission = new Submission();
            submission.setUserId(request.getUserId());
            submission.setProblemId(request.getProblemId());
            submission.setLanguage(request.getLanguage().toString());
            submission.setCode(request.getCode());
            submission.setVerdict(verdict);
            submission.setScore(score);
            submission.setExecutionTime(executionTime);
            submission.setPassedTests(passedTests);
            submission.setTotalTests(totalTests);
            submission.setSubmittedAt(LocalDateTime.now());
            
            submissionRepository.save(submission);
        } catch (Exception e) {
            // Log error but don't fail the evaluation
            System.err.println("Failed to save submission: " + e.getMessage());
        }
    }
    
    private String readProcessOutput(Process process) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            StringBuilder output = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
            return output.toString();
        }
    }
    
    private String readProcessError(Process process) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
            StringBuilder error = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                error.append(line).append("\n");
            }
            return error.toString();
        }
    }
    
    private void cleanupDirectory(Path directory) {
        try {
            Files.walk(directory)
                .sorted(Comparator.reverseOrder())
                .map(Path::toFile)
                .forEach(File::delete);
        } catch (Exception e) {
            System.err.println("Failed to cleanup directory: " + e.getMessage());
        }
    }
    
    // Helper class for compilation results
    private static class CompilationResult {
        private final boolean success;
        private final String error;
        
        public CompilationResult(boolean success, String error) {
            this.success = success;
            this.error = error;
        }
        
        public boolean isSuccess() { return success; }
        public String getError() { return error; }
    }
}
