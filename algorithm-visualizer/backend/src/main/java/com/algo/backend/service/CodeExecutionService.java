package com.algo.backend.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.algo.backend.entity.Algorithm;
import com.algo.backend.entity.CodeSubmission;
import com.algo.backend.entity.User;
import com.algo.backend.repository.AlgorithmRepository;
import com.algo.backend.repository.CodeSubmissionRepository;
import com.algo.backend.repository.UserRepository;

import reactor.core.publisher.Mono;

@Service
public class CodeExecutionService {

    @Autowired
    private CodeSubmissionRepository codeSubmissionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AlgorithmRepository algorithmRepository;

    @Value("${judge0.enabled:false}")
    private boolean judge0Enabled;

    @Value("${judge0.api.url:http://localhost:2358}")
    private String judge0ApiUrl;

    @Value("${judge0.api.key:}")
    private String judge0ApiKey;

    private final WebClient webClient;

    public CodeExecutionService() {
        this.webClient = WebClient.builder().build();
    }

    public CodeSubmission submitCode(Long userId, Long algorithmId, String sourceCode, CodeSubmission.Language language) {
        if (!judge0Enabled) {
            throw new UnsupportedOperationException("Code execution is currently disabled. Please enable Judge0 in the configuration.");
        }

        Optional<User> userOpt = userRepository.findById(userId);
        Optional<Algorithm> algorithmOpt = algorithmRepository.findById(algorithmId);

        if (userOpt.isPresent() && algorithmOpt.isPresent()) {
            User user = userOpt.get();
            Algorithm algorithm = algorithmOpt.get();

            CodeSubmission submission = new CodeSubmission(user, algorithm, sourceCode, language);
            submission = codeSubmissionRepository.save(submission);

            // Execute code asynchronously
            executeCodeAsync(submission);

            return submission;
        }
        return null;
    }

    private void executeCodeAsync(CodeSubmission submission) {
        int languageId = getJudge0LanguageId(submission.getLanguage());

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("source_code", submission.getSourceCode());
        requestBody.put("language_id", languageId);
        requestBody.put("stdin", ""); // Add test input if needed

        webClient.post()
                .uri(judge0ApiUrl + "/submissions")
                .header("X-RapidAPI-Key", judge0ApiKey)
                .header("X-RapidAPI-Host", "judge0-ce.p.rapidapi.com")
                .header("Content-Type", "application/json")
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(new org.springframework.core.ParameterizedTypeReference<Map<String, Object>>() {})
                .flatMap(response -> {
                    String token = (String) response.get("token");
                    submission.setJudge0Token(token);
                    codeSubmissionRepository.save(submission);

                    return pollForResults(submission, token);
                })
                .subscribe();
    }

    private Mono<Void> pollForResults(CodeSubmission submission, String token) {
        return webClient.get()
                .uri(judge0ApiUrl + "/submissions/" + token)
                .header("X-RapidAPI-Key", judge0ApiKey)
                .header("X-RapidAPI-Host", "judge0-ce.p.rapidapi.com")
                .retrieve()
                .bodyToMono(new org.springframework.core.ParameterizedTypeReference<Map<String, Object>>() {})
                .flatMap(response -> {
                    Object statusObj = response.get("status");
                    if (!(statusObj instanceof Map<?, ?> status)) {
                        return Mono.error(new RuntimeException("Invalid response format"));
                    }

                    Object idObj = status.get("id");
                    if (!(idObj instanceof Integer statusId)) {
                        return Mono.error(new RuntimeException("Invalid status ID format"));
                    }

                    if (statusId <= 2) {
                        return Mono.delay(java.time.Duration.ofSeconds(2))
                                .then(pollForResults(submission, token));
                    } else {
                        updateSubmissionResults(submission, response);
                        return Mono.empty();
                    }
                });
    }

    private void updateSubmissionResults(CodeSubmission submission, Map<String, Object> response) {
        Object statusObj = response.get("status");
        if (!(statusObj instanceof Map<?, ?> status)) {
            throw new RuntimeException("Invalid response format");
        }

        Object idObj = status.get("id");
        if (!(idObj instanceof Integer statusId)) {
            throw new RuntimeException("Invalid status ID format");
        }

        CodeSubmission.Status submissionStatus = mapJudge0Status(statusId);
        submission.setStatus(submissionStatus);

        // Execution time
        Object timeObj = response.get("time");
        switch (timeObj) {
            case String s -> submission.setExecutionTime(Double.valueOf(s));
            case Number num -> submission.setExecutionTime(num.doubleValue());
            case null, default -> {}
        }

        // Memory usage
        Object memoryObj = response.get("memory");
        switch (memoryObj) {
            case String s -> submission.setMemoryUsage(Integer.valueOf(s));
            case Number num -> submission.setMemoryUsage(num.intValue());
            case null, default -> {}
        }

        // Error message
        Object stderrObj = response.get("stderr");
        if (stderrObj != null) {
            submission.setErrorMessage(stderrObj.toString());
        }

        // Program output
        Object stdoutObj = response.get("stdout");
        if (stdoutObj != null) {
            submission.setOutput(stdoutObj.toString());
        }

        // Reward user for accepted solutions
        if (submissionStatus == CodeSubmission.Status.ACCEPTED) {
            User user = submission.getUser();
            user.setTotalScore(user.getTotalScore() + 50);
            userRepository.save(user);
        }

        codeSubmissionRepository.save(submission);
    }

    private int getJudge0LanguageId(CodeSubmission.Language language) {
        return switch (language) {
            case JAVA -> 62;
            case PYTHON -> 71;
            case CPP -> 54;
            case JAVASCRIPT -> 63;
        };
    }

    private CodeSubmission.Status mapJudge0Status(int statusId) {
        return switch (statusId) {
            case 3 -> CodeSubmission.Status.ACCEPTED;
            case 4 -> CodeSubmission.Status.WRONG_ANSWER;
            case 5 -> CodeSubmission.Status.TIME_LIMIT_EXCEEDED;
            case 6 -> CodeSubmission.Status.COMPILATION_ERROR;
            case 7, 8, 9, 10, 11, 12 -> CodeSubmission.Status.RUNTIME_ERROR;
            default -> CodeSubmission.Status.PENDING;
        };
    }

    public List<CodeSubmission> getUserSubmissions(Long userId) {
        return codeSubmissionRepository.findByUserIdOrderBySubmittedAtDesc(userId);
    }

    public List<CodeSubmission> getAcceptedSubmissions(Long userId) {
        return codeSubmissionRepository.findAcceptedSubmissionsByUser(userId);
    }

    public CodeSubmission getBestSubmission(Long userId, Long algorithmId) {
        var results = codeSubmissionRepository.findBestSubmissionsByUserAndAlgorithm(
                userId,
                algorithmId,
                PageRequest.of(0, 1)
        );
        return results.isEmpty() ? null : results.get(0);
    }
}
