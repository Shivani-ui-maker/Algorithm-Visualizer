package com.algo.backend.controller;

import com.algo.backend.entity.Algorithm;
import com.algo.backend.entity.AlgorithmCategory;
import com.algo.backend.entity.AlgorithmContent;
import com.algo.backend.service.AlgorithmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/algorithms")
public class AlgorithmController {

    @Autowired
    private AlgorithmService algorithmService;

    @GetMapping("/public/all")
    public ResponseEntity<List<Algorithm>> getAllAlgorithms() {
        List<Algorithm> algorithms = algorithmService.getAllAlgorithms();
        return ResponseEntity.ok(algorithms);
    }

    @GetMapping("/public/{id}")
    public ResponseEntity<Algorithm> getAlgorithmById(@PathVariable Long id) {
        Optional<Algorithm> algorithm = algorithmService.getAlgorithmById(id);
        return algorithm.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/public/slug/{slug}")
    public ResponseEntity<Algorithm> getAlgorithmBySlug(@PathVariable String slug) {
        Optional<Algorithm> algorithm = algorithmService.getAlgorithmBySlug(slug);
        return algorithm.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/public/name/{name}")
    public ResponseEntity<Algorithm> getAlgorithmByName(@PathVariable String name) {
        Optional<Algorithm> algorithm = algorithmService.getAlgorithmByName(name);
        return algorithm.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/public/difficulty/{difficulty}")
    public ResponseEntity<List<Algorithm>> getAlgorithmsByDifficulty(@PathVariable Algorithm.Difficulty difficulty) {
        List<Algorithm> algorithms = algorithmService.getAlgorithmsByDifficulty(difficulty);
        return ResponseEntity.ok(algorithms);
    }

    @GetMapping("/public/categories")
    public ResponseEntity<List<AlgorithmCategory>> getAllCategories() {
        List<AlgorithmCategory> categories = algorithmService.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/public/{id}/content")
    public ResponseEntity<AlgorithmContent> getAlgorithmContent(@PathVariable Long id) {
        AlgorithmContent content = algorithmService.getAlgorithmContent(id);
        return content != null ? ResponseEntity.ok(content) : ResponseEntity.notFound().build();
    }

    @GetMapping("/public/random/{limit}")
    public ResponseEntity<List<Algorithm>> getRandomAlgorithms(@PathVariable int limit) {
        List<Algorithm> algorithms = algorithmService.getRandomAlgorithms(limit);
        return ResponseEntity.ok(algorithms);
    }

    @PostMapping("/admin/create")
    public ResponseEntity<Algorithm> createAlgorithm(@RequestBody Algorithm algorithm) {
        Algorithm savedAlgorithm = algorithmService.saveAlgorithm(algorithm);
        return ResponseEntity.ok(savedAlgorithm);
    }

    @PutMapping("/admin/{id}")
    public ResponseEntity<Algorithm> updateAlgorithm(@PathVariable Long id, @RequestBody Algorithm algorithmDetails) {
        Algorithm updatedAlgorithm = algorithmService.updateAlgorithm(id, algorithmDetails);
        if (updatedAlgorithm != null) {
            return ResponseEntity.ok(updatedAlgorithm);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/admin/{id}")
    public ResponseEntity<?> deleteAlgorithm(@PathVariable Long id) {
        algorithmService.deleteAlgorithm(id);
        return ResponseEntity.ok().build();
    }
}
