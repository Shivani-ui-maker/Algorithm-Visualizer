package com.algo.backend.controller;

import com.algo.backend.service.VisualizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/visualization")
public class VisualizationController {

    @Autowired
    private VisualizationService visualizationService;

    /**
     * Generate visualization steps for sorting algorithms
     */
    @PostMapping("/sorting/{algorithmName}")
    public ResponseEntity<Map<String, Object>> generateSortingVisualization(
            @PathVariable String algorithmName,
            @RequestBody Map<String, Object> request) {
        
        try {
            int[] array = (int[]) request.get("array");
            if (array == null) {
                // Convert from Integer array or List if needed
                Object arrayObj = request.get("array");
                if (arrayObj instanceof java.util.List) {
                    java.util.List<?> list = (java.util.List<?>) arrayObj;
                    array = list.stream().mapToInt(x -> (Integer) x).toArray();
                } else {
                    return ResponseEntity.badRequest().body(Map.of("error", "Invalid array format"));
                }
            }
            
            Map<String, Object> result = visualizationService.generateSortingVisualization(algorithmName, array);
            return ResponseEntity.ok(result);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", "Internal server error: " + e.getMessage()));
        }
    }

    /**
     * Generate visualization steps for searching algorithms
     */
    @PostMapping("/searching/{algorithmName}")
    public ResponseEntity<Map<String, Object>> generateSearchVisualization(
            @PathVariable String algorithmName,
            @RequestBody Map<String, Object> request) {
        
        try {
            int[] array = (int[]) request.get("array");
            Integer target = (Integer) request.get("target");
            
            if (array == null) {
                // Convert from Integer array or List if needed
                Object arrayObj = request.get("array");
                if (arrayObj instanceof java.util.List) {
                    java.util.List<?> list = (java.util.List<?>) arrayObj;
                    array = list.stream().mapToInt(x -> (Integer) x).toArray();
                } else {
                    return ResponseEntity.badRequest().body(Map.of("error", "Invalid array format"));
                }
            }
            
            if (target == null) {
                return ResponseEntity.badRequest().body(Map.of("error", "Target value is required"));
            }
            
            Map<String, Object> result = visualizationService.generateSearchVisualization(algorithmName, array, target);
            return ResponseEntity.ok(result);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", "Internal server error: " + e.getMessage()));
        }
    }

    /**
     * Generate visualization steps for graph algorithms
     */
    @PostMapping("/graph/{algorithmName}")
    public ResponseEntity<Map<String, Object>> generateGraphVisualization(
            @PathVariable String algorithmName,
            @RequestBody Map<String, Object> graphData) {
        
        try {
            Map<String, Object> result = visualizationService.generateGraphVisualization(algorithmName, graphData);
            return ResponseEntity.ok(result);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", "Internal server error: " + e.getMessage()));
        }
    }

    /**
     * Generate random array for visualization
     */
    @GetMapping("/generate-array")
    public ResponseEntity<Map<String, Object>> generateRandomArray(
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "1") int min,
            @RequestParam(defaultValue = "100") int max) {
        
        try {
            if (size <= 0 || size > 50) {
                return ResponseEntity.badRequest().body(Map.of("error", "Array size must be between 1 and 50"));
            }
            
            if (min >= max) {
                return ResponseEntity.badRequest().body(Map.of("error", "Min value must be less than max value"));
            }
            
            int[] array = new int[size];
            java.util.Random random = new java.util.Random();
            
            for (int i = 0; i < size; i++) {
                array[i] = random.nextInt(max - min + 1) + min;
            }
            
            Map<String, Object> result = Map.of(
                "array", array,
                "size", size,
                "min", min,
                "max", max
            );
            
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", "Internal server error: " + e.getMessage()));
        }
    }

    /**
     * Generate sorted array for binary search testing
     */
    @GetMapping("/generate-sorted-array")
    public ResponseEntity<Map<String, Object>> generateSortedArray(
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "1") int min,
            @RequestParam(defaultValue = "100") int max) {
        
        try {
            if (size <= 0 || size > 50) {
                return ResponseEntity.badRequest().body(Map.of("error", "Array size must be between 1 and 50"));
            }
            
            if (min >= max) {
                return ResponseEntity.badRequest().body(Map.of("error", "Min value must be less than max value"));
            }
            
            int[] array = new int[size];
            java.util.Random random = new java.util.Random();
            
            // Generate random array first
            for (int i = 0; i < size; i++) {
                array[i] = random.nextInt(max - min + 1) + min;
            }
            
            // Sort the array
            java.util.Arrays.sort(array);
            
            Map<String, Object> result = Map.of(
                "array", array,
                "size", size,
                "min", min,
                "max", max,
                "sorted", true
            );
            
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", "Internal server error: " + e.getMessage()));
        }
    }

    /**
     * Validate algorithm support
     */
    @GetMapping("/supported-algorithms")
    public ResponseEntity<Map<String, Object>> getSupportedAlgorithms() {
        Map<String, Object> algorithms = Map.of(
            "sorting", java.util.Arrays.asList(
                "bubble-sort", "selection-sort", "insertion-sort", 
                "merge-sort", "quick-sort", "heap-sort"
            ),
            "searching", java.util.Arrays.asList(
                "linear-search", "binary-search", "jump-search"
            ),
            "graph", java.util.Arrays.asList(
                "bfs", "dfs", "dijkstra"
            )
        );
        
        return ResponseEntity.ok(algorithms);
    }

    /**
     * Get algorithm complexity information
     */
    @GetMapping("/complexity/{algorithmName}")
    public ResponseEntity<Map<String, Object>> getAlgorithmComplexity(@PathVariable String algorithmName) {
        Map<String, Map<String, String>> complexities = Map.of(
            "bubble-sort", Map.of(
                "timeWorst", "O(n²)",
                "timeAverage", "O(n²)",
                "timeBest", "O(n)",
                "space", "O(1)"
            ),
            "selection-sort", Map.of(
                "timeWorst", "O(n²)",
                "timeAverage", "O(n²)",
                "timeBest", "O(n²)",
                "space", "O(1)"
            ),
            "insertion-sort", Map.of(
                "timeWorst", "O(n²)",
                "timeAverage", "O(n²)",
                "timeBest", "O(n)",
                "space", "O(1)"
            ),
            "merge-sort", Map.of(
                "timeWorst", "O(n log n)",
                "timeAverage", "O(n log n)",
                "timeBest", "O(n log n)",
                "space", "O(n)"
            ),
            "quick-sort", Map.of(
                "timeWorst", "O(n²)",
                "timeAverage", "O(n log n)",
                "timeBest", "O(n log n)",
                "space", "O(log n)"
            ),
            "heap-sort", Map.of(
                "timeWorst", "O(n log n)",
                "timeAverage", "O(n log n)",
                "timeBest", "O(n log n)",
                "space", "O(1)"
            ),
            "linear-search", Map.of(
                "timeWorst", "O(n)",
                "timeAverage", "O(n)",
                "timeBest", "O(1)",
                "space", "O(1)"
            ),
            "binary-search", Map.of(
                "timeWorst", "O(log n)",
                "timeAverage", "O(log n)",
                "timeBest", "O(1)",
                "space", "O(1)"
            ),
            "jump-search", Map.of(
                "timeWorst", "O(√n)",
                "timeAverage", "O(√n)",
                "timeBest", "O(1)",
                "space", "O(1)"
            )
        );
        
        Map<String, String> complexity = complexities.get(algorithmName.toLowerCase());
        if (complexity == null) {
            return ResponseEntity.notFound().build();
        }
        
        Map<String, Object> result = Map.of(
            "algorithm", algorithmName,
            "complexity", complexity
        );
        
        return ResponseEntity.ok(result);
    }

    /**
     * Health check endpoint
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> healthCheck() {
        return ResponseEntity.ok(Map.of(
            "status", "UP",
            "service", "VisualizationService",
            "timestamp", java.time.Instant.now().toString()
        ));
    }
}
