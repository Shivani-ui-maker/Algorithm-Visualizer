package com.algo.backend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;

@RestController
@RequestMapping("/api/test")
@CrossOrigin(origins = "*")
public class TestController {

    @GetMapping("/health")
    public String health() {
        return "Backend is running successfully!";
    }

    @GetMapping("/status")
    public java.util.Map<String, Object> status() {
        return java.util.Map.of(
            "status", "OK",
            "message", "Algorithm Visualizer Backend",
            "timestamp", System.currentTimeMillis()
        );
    }
}
