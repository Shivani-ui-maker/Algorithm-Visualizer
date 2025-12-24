package com.algo.backend.controller;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/code")
public class CodeEditorController {

    private static final Logger logger = LoggerFactory.getLogger(CodeEditorController.class);

    private final List<Map<String, Object>> problems = new ArrayList<>();
    private int currentProblemIndex = 0;

    private static final String JUDGE0_API_URL = "https://judge0-ce.p.rapidapi.com/submissions?base64_encoded=false&wait=true";
    private static final String RAPIDAPI_HOST = "judge0-ce.p.rapidapi.com";
    private static final String RAPIDAPI_KEY = "78083e92e8mshd7e19bc6ea8dde5p19c4f0jsn3401e7af5586";

    private final ObjectMapper mapper = new ObjectMapper();
    private final HttpClient httpClient = HttpClient.newHttpClient();

    public CodeEditorController() {
        // Initialize all story-type problems
        addProblem(
                "Classroom Attendance",
                "There are 30 students in a class. Each student marks attendance as either present or absent. Write a function to count how many students are present today.",
                """
                        public class Solution {
                            public static int countAttendance(boolean[] attendance) {
                                // TODO
                                return 0;
                            }
                        }
                        """,
                "java"
        );

        addProblem(
                "Student Grades Average",
                "A teacher records marks for 5 subjects for each student. Compute the average mark per student.",
                """
                        public class Solution {
                            public static double averageMarks(int[] marks) {
                                // TODO
                                return 0;
                            }
                        }
                        """,
                "java"
        );

        addProblem(
                "Find the Tallest Student",
                "The class wants to select a team leader. Find the tallest student from the height list.",
                """
                        public class Solution {
                            public static int tallestStudent(int[] heights) {
                                // TODO
                                return 0;
                            }
                        }
                        """,
                "java"
        );

        addProblem(
                "Even Number Collection",
                "In a classroom, the teacher asks 10 students to pick numbers from a basket. Write a function to calculate the sum of all even numbers selected today.",
                """
                        public class Solution {
                            public static int sumEven(int[] nums) {
                                // TODO
                                return 0;
                            }
                        }
                        """,
                "java"
        );

        addProblem(
                "Reversing Student Queue",
                "Students are lining up for lunch. The teacher wants to reverse the order of the queue to manage seating. Write a function to reverse the array of student IDs.",
                """
                        public class Solution {
                            public static void reverseArray(int[] arr) {
                                // TODO
                            }
                        }
                        """,
                "java"
        );

        addProblem(
                "Class Project Scores",
                "A group of students received scores for a project. Find the contiguous group of scores that gives the highest total score, to reward the most consistent group.",
                """
                        public class Solution {
                            public static int maxSubarraySum(int[] nums) {
                                // TODO
                                return 0;
                            }
                        }
                        """,
                "java"
        );

        addProblem(
                "Fibonacci Playground",
                "In a coding club, students are asked to create a pattern using the first n Fibonacci numbers. Write a function to generate these numbers.",
                """
                        public class Solution {
                            public static int[] fibonacci(int n) {
                                // TODO
                                return new int[0];
                            }
                        }
                        """,
                "java"
        );

        addProblem(
                "Mirror Words",
                "A teacher wants students to check if a given word is spelled the same forwards and backwards for a language puzzle. Write a function to verify it.",
                """
                        public class Solution {
                            public static boolean isPalindrome(String str) {
                                // TODO
                                return false;
                            }
                        }
                        """,
                "java"
        );

        addProblem(
                "Prime Age List",
                "In a school, the principal wants to list all students whose ages are prime numbers below a certain value n. Write a function to return all prime numbers less than n.",
                """
                        import java.util.*;
                        public class Solution {
                            public static List<Integer> primes(int n) {
                                // TODO
                                return new ArrayList<>();
                            }
                        }
                        """,
                "java"
        );

        addProblem(
                "Factorial Celebration",
                "In a math club, students are asked to compute the factorial of a number to prepare a pattern for decorations. Write a recursive function to calculate the factorial of n.",
                """
                        public class Solution {
                            public static long factorial(int n) {
                                // TODO
                                return 1;
                            }
                        }
                        """,
                "java"
        );

        addProblem(
                "Sentence Reversal Game",
                "During a language game, the teacher gives a sentence and asks students to reverse the order of words for fun. Write a function to reverse the words in a given sentence.",
                """
                        public class Solution {
                            public static String reverseWords(String sentence) {
                                // TODO
                                return "";
                            }
                        }
                        """,
                "java"
        );

        addProblem(
                "Vowel Counting Challenge",
                "A teacher asks students to count how many vowels appear in a student's name list to practice string processing. Write a function to count the vowels in a string.",
                """
                        public class Solution {
                            public static int countVowels(String str) {
                                // TODO
                                return 0;
                            }
                        }
                        """,
                "java"
        );

        addProblem(
                "Merge Class Scores",
                "Two classes submitted their test scores separately. The principal wants to merge these sorted lists to make a single ranked list. Write a function to merge two sorted arrays.",
                """
                        public class Solution {
                            public static int[] mergeSorted(int[] a, int[] b) {
                                // TODO
                                return new int[0];
                            }
                        }
                        """,
                "java"
        );

        addProblem(
                "Unique Student IDs",
                "A teacher has a list of student IDs, but some IDs are repeated by mistake. Write a function to remove duplicate IDs from the list.",
                """
                        import java.util.*;
                        public class Solution {
                            public static int[] removeDuplicates(int[] nums) {
                                // TODO
                                return new int[0];
                            }
                        }
                        """,
                "java"
        );

        addProblem(
                "Word Counting Contest",
                "In a writing contest, students submit essays. The teacher wants to count the number of words in each essay. Write a function to count words in a sentence.",
                """
                        public class Solution {
                            public static int countWords(String sentence) {
                                // TODO
                                return 0;
                            }
                        }
                        """,
                "java"
        );

        addProblem(
                "Second Tallest Student",
                "In a sports team selection, the coach wants to find the second tallest student. Write a function to find the second largest number in a list of student heights.",
                """
                        public class Solution {
                            public static int secondLargest(int[] nums) {
                                // TODO
                                return 0;
                            }
                        }
                        """,
                "java"
        );

        addProblem(
                "Classroom Seating Plan",
                "The teacher wants to rotate the classroom seating. Compute the transpose of the seating arrangement represented as a 2D matrix.",
                """
                        public class Solution {
                            public static int[][] transpose(int[][] matrix) {
                                // TODO
                                return new int[0][0];
                            }
                        }
                        """,
                "java"
        );

        addProblem(
                "Treasure Hunt Search",
                "In a treasure hunt game, students have a sorted list of clues. Write a function to quickly find the position of a specific clue using binary search.",
                """
                        public class Solution {
                            public static int binarySearch(int[] nums, int target) {
                                // TODO
                                return -1;
                            }
                        }
                        """,
                "java"
        );

        addProblem(
                "Sorting Student Scores",
                "A teacher wants to rank students based on their scores using a simple method. Write a function to sort an array of scores using the bubble sort algorithm.",
                """
                        public class Solution {
                            public static void bubbleSort(int[] nums) {
                                // TODO
                            }
                        }
                        """,
                "java"
        );

        addProblem(
                "Light Bulb Counter",
                "A science experiment involves light bulbs that can be ON or OFF. Write a function to count the number of ON bulbs (set bits) in the binary representation of a number.",
                """
                        public class Solution {
                            public static int countSetBits(int n) {
                                // TODO
                                return 0;
                            }
                        }
                        """,
                "java"
        );
    }

    private void addProblem(String title, String description, String starterCode, String language) {
        Map<String, Object> problem = new HashMap<>();
        problem.put("title", title);
        problem.put("description", description);
        problem.put("starterCode", starterCode);
        problem.put("language", language);
        problem.put("hints", List.of("Think about loops", "Use arrays/lists", "Consider edge cases"));
        problems.add(problem);
    }

    @GetMapping("/problem")
    public ResponseEntity<?> getCurrentProblem() {
        if (problems.isEmpty()) return ResponseEntity.ok(Map.of("message", "No problems available"));
        return ResponseEntity.ok(problems.get(currentProblemIndex));
    }

    @PostMapping("/submit")
    public ResponseEntity<?> submitSolution(@RequestBody Map<String, Object> request) {
        try {
            String code = (String) request.getOrDefault("code", "");
            String language = (String) request.getOrDefault("language", "python");

            if (code.isBlank()) {
                return ResponseEntity.ok(Map.of(
                        "success", false,
                        "message", "Code cannot be empty",
                        "hints", List.of("Write some code in the editor")
                ));
            }

            int languageId = mapLanguageToJudge0Id(language);

            Map<String, Object> payload = Map.of(
                    "source_code", code,
                    "language_id", languageId
            );
            String jsonPayload = mapper.writeValueAsString(payload);

            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create(JUDGE0_API_URL))
                    .header("Content-Type", "application/json")
                    .header("x-rapidapi-host", RAPIDAPI_HOST)
                    .header("x-rapidapi-key", RAPIDAPI_KEY)
                    .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                    .build();

            HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

            Map<String, Object> result = mapper.readValue(response.body(), new TypeReference<>() {});

            boolean passed = false;
            Object statusObj = result.get("status");
            if (statusObj instanceof Map<?, ?> statusMap) {
                Object descriptionObj = statusMap.get("description");
                if (descriptionObj != null) {
                    passed = "Accepted".equalsIgnoreCase(descriptionObj.toString());
                }
            }

            List<String> hints = new ArrayList<>();
            if (!passed) {
                Object stderr = result.get("stderr");
                Object compileOutput = result.get("compile_output");
                if (stderr != null) hints.add(stderr.toString());
                if (compileOutput != null) hints.add(compileOutput.toString());
                if (hints.isEmpty()) hints.add("Check your logic and syntax.");
            }

            if (passed) currentProblemIndex = (currentProblemIndex + 1) % problems.size();

            return ResponseEntity.ok(Map.of(
                    "success", passed,
                    "message", passed ? "Solution passed!" : "Solution failed",
                    "hints", hints,
                    "nextProblem", problems.get(currentProblemIndex),
                    "judgeResult", result
            ));

        } catch (JsonProcessingException e) {
            logger.error("Error parsing Judge0 response: {}", e.getMessage(), e);
            return ResponseEntity.ok(Map.of(
                    "success", false,
                    "message", "Error parsing Judge0 response",
                    "hints", List.of("Check code syntax or network connection")
            ));
        } catch (IOException | InterruptedException e) {
            logger.error("Error during HTTP request: {}", e.getMessage(), e);
            return ResponseEntity.ok(Map.of(
                    "success", false,
                    "message", "Error sending request to Judge0 API",
                    "hints", List.of("Check network connection")
            ));
        } catch (RuntimeException e) {
            logger.error("Runtime error: {}", e.getMessage(), e);
            return ResponseEntity.ok(Map.of(
                    "success", false,
                    "message", "Unexpected runtime error during code evaluation",
                    "hints", List.of("Check your logic")
            ));
        }
    }

    private int mapLanguageToJudge0Id(String language) {
        String lang = language == null ? "" : language.toLowerCase();
        if (lang.equals("python") || lang.equals("python3") || lang.equals("py")) return 71;
        if (lang.equals("java")) return 62;
        if (lang.equals("cpp") || lang.equals("c++")) return 54;
        return 71;
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllProblems() {
        return ResponseEntity.ok(problems);
    }
}
