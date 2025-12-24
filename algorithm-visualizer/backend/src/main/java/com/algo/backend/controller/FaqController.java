package com.algo.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/faq")
@CrossOrigin(origins = {"http://localhost:4200", "http://localhost:4201"})
public class FaqController {

    @PostMapping("/submit")
    public ResponseEntity<?> submitFaqAnswer(@RequestBody Map<String, Object> request) {
        try {
            String question = (String) request.get("question");
            String answer = (String) request.get("answer");
            Integer timeTaken = (Integer) request.get("timeTaken");

            // Here you can save the FAQ submission to database
            // For now, just return success response

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "FAQ answer submitted successfully");
            response.put("question", question); // Include question in response for confirmation
            response.put("score", calculateScore(answer, timeTaken));

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Error submitting FAQ answer: " + e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
    
    private int calculateScore(String answer, Integer timeTaken) {
        // Simple scoring logic - can be enhanced
        int baseScore = answer != null && !answer.trim().isEmpty() ? 50 : 0;
        int timeBonus = timeTaken != null && timeTaken < 60 ? 20 : 10;
        return baseScore + timeBonus;
    }
}
