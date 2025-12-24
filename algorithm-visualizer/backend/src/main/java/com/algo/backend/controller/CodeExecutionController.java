package com.algo.backend.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import com.algo.backend.entity.CodeSubmission;
import com.algo.backend.service.CodeExecutionService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/execution")
public class CodeExecutionController {

    @Autowired
    private CodeExecutionService codeExecutionService;

    @Value("${judge0.enabled:false}")
    private boolean judge0Enabled;

    @Value("${judge0.api.url:http://localhost:2358}")
    private String judge0ApiUrl;

    @Value("${judge0.api.key:}")
    private String judge0ApiKey;

    private final WebClient webClient = WebClient.builder().build();

    @PostMapping("/submit")
    public ResponseEntity<?> submitCode(
            @RequestBody Map<String, Object> request,
            Authentication authentication) {
        
        if (authentication == null) {
            return ResponseEntity.badRequest().body("Authentication required");
        }

        try {
            Long userId = Long.valueOf(request.get("userId").toString());
            Long algorithmId = Long.valueOf(request.get("algorithmId").toString());
            String sourceCode = request.get("sourceCode").toString();
            CodeSubmission.Language language = CodeSubmission.Language.valueOf(request.get("language").toString());

            if (sourceCode.isBlank()) {
                return ResponseEntity.badRequest().body("Source code cannot be empty.");
            }

            CodeSubmission submission = codeExecutionService.submitCode(userId, algorithmId, sourceCode, language);
            if (submission != null) {
                return ResponseEntity.ok(submission);
            }
            return ResponseEntity.badRequest().body("Failed to submit code");
        } catch (UnsupportedOperationException e) {
            return ResponseEntity.status(503).body("Code execution service is currently disabled. Please contact the administrator.");
        } catch (IllegalArgumentException | NullPointerException | ClassCastException e) {
            return ResponseEntity.badRequest().body("Invalid request: " + e.getMessage());
        }
    }

    // Synchronous code execution for the frontend code editor
    @PostMapping("/execute")
    public ResponseEntity<?> execute(@RequestBody Map<String, Object> request, Authentication authentication) {
        if (!judge0Enabled) {
            return ResponseEntity.status(503).body("Code execution is disabled. Enable judge0.enabled=true in application.yml");
        }

        // Optional: enforce authentication
        if (authentication == null) {
            return ResponseEntity.status(401).body("Authentication required to execute code");
        }

        try {
            String code = (String) request.getOrDefault("source_code", "");
            if (code.isBlank()) {
                return ResponseEntity.badRequest().body("Code cannot be empty");
            }

            String language = (String) request.getOrDefault("language", "java");
            int languageId = mapLanguageToJudge0Id(language);

            Map<String, Object> body = new HashMap<>();
            body.put("source_code", code);
            body.put("language_id", languageId);
            body.put("stdin", request.getOrDefault("stdin", ""));

            Map<String, Object> response = webClient.post()
                    .uri(judge0ApiUrl + "/submissions?base64_encoded=false&wait=true")
                    .header("Content-Type", "application/json")
                    .headers(headers -> {
                        if (judge0ApiKey != null && !judge0ApiKey.isBlank()) {
                            headers.set("X-RapidAPI-Key", judge0ApiKey);
                            headers.set("X-RapidAPI-Host", "judge0-ce.p.rapidapi.com");
                        }
                    })
                    .bodyValue(body)
                    .retrieve()
                    .bodyToMono(new org.springframework.core.ParameterizedTypeReference<Map<String, Object>>() {})
                    .block();

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.status(502).body("Judge0 proxy error: " + e.getMessage());
        }
    }

    // Judge0 /about passthrough for quick health check
    @GetMapping("/about")
    public ResponseEntity<?> about() {
        try {
            Map<String, Object> response = webClient.get()
                    .uri(judge0ApiUrl + "/about")
                    .headers(headers -> {
                        if (judge0ApiKey != null && !judge0ApiKey.isBlank()) {
                            headers.set("X-RapidAPI-Key", judge0ApiKey);
                            headers.set("X-RapidAPI-Host", "judge0-ce.p.rapidapi.com");
                        }
                    })
                    .retrieve()
                    .bodyToMono(new org.springframework.core.ParameterizedTypeReference<Map<String, Object>>() {})
                    .block();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(502).body("Judge0 about proxy error: " + e.getMessage());
        }
    }

    @GetMapping("/submissions/{userId}")
    public ResponseEntity<List<CodeSubmission>> getUserSubmissions(@PathVariable Long userId) {
        List<CodeSubmission> submissions = codeExecutionService.getUserSubmissions(userId);
        return ResponseEntity.ok(submissions);
    }

    @GetMapping("/submissions/{userId}/accepted")
    public ResponseEntity<List<CodeSubmission>> getAcceptedSubmissions(@PathVariable Long userId) {
        List<CodeSubmission> submissions = codeExecutionService.getAcceptedSubmissions(userId);
        return ResponseEntity.ok(submissions);
    }

    @GetMapping("/submissions/{userId}/algorithm/{algorithmId}/best")
    public ResponseEntity<CodeSubmission> getBestSubmission(@PathVariable Long userId, @PathVariable Long algorithmId) {
        CodeSubmission submission = codeExecutionService.getBestSubmission(userId, algorithmId);
        if (submission != null) {
            return ResponseEntity.ok(submission);
        }
        return ResponseEntity.notFound().build();
    }

    private int mapLanguageToJudge0Id(String language) {
        String lang = language == null ? "" : language.toLowerCase();
        return switch (lang) {
            case "python", "python3", "py" -> 71;
            case "java" -> 62;
            case "cpp", "c++" -> 54;
            default -> 71; // default to Python
        };
    }
}
