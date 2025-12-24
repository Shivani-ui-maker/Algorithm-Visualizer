package com.algoviz.codeexecution.service;

import com.algoviz.codeexecution.dto.ProblemDTO;
import com.algoviz.codeexecution.dto.TestCaseDTO;
import com.algoviz.codeexecution.dto.StarterCodeDTO;
import com.algoviz.codeexecution.entity.Problem;
import com.algoviz.codeexecution.entity.TestCase;
import com.algoviz.codeexecution.entity.StarterCode;
import com.algoviz.codeexecution.repository.ProblemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProblemService {

    @Autowired
    private ProblemRepository problemRepository;

    public List<ProblemDTO> getAllProblems() {
        List<Problem> problems = problemRepository.findAll();
        return problems.stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    public ProblemDTO getProblemById(Long id) {
        Problem problem = problemRepository.findByIdWithAll(id)
            .orElseThrow(() -> new RuntimeException("Problem not found with id: " + id));
        return convertToDTO(problem);
    }

    public Problem getProblemEntityById(Long id) {
        return problemRepository.findByIdWithTestCases(id)
            .orElseThrow(() -> new RuntimeException("Problem not found with id: " + id));
    }

    public List<ProblemDTO> getProblemsByDifficulty(Problem.Difficulty difficulty) {
        List<Problem> problems = problemRepository.findByDifficulty(difficulty);
        return problems.stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    public List<ProblemDTO> getProblemsByCategory(String category) {
        List<Problem> problems = problemRepository.findByCategory(category);
        return problems.stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    private ProblemDTO convertToDTO(Problem problem) {
        ProblemDTO dto = new ProblemDTO();
        dto.setId(problem.getId());
        dto.setTitle(problem.getTitle());
        dto.setDescription(problem.getDescription());
        dto.setScenario(problem.getScenario());
        dto.setInputFormat(problem.getInputFormat());
        dto.setOutputFormat(problem.getOutputFormat());
        dto.setHints(problem.getHints());
        dto.setDifficulty(problem.getDifficulty());
        dto.setCategory(problem.getCategory());
        dto.setTimeLimit(problem.getTimeLimit());
        dto.setMemoryLimit(problem.getMemoryLimit());
        dto.setMaxScore(problem.getMaxScore());
        dto.setXpReward(problem.getXpReward());

        if (problem.getTestCases() != null) {
            List<TestCaseDTO> testCaseDTOs = problem.getTestCases().stream()
                .map(this::convertTestCaseToDTO)
                .collect(Collectors.toList());
            dto.setTestCases(testCaseDTOs);
        }

        if (problem.getStarterCodes() != null) {
            List<StarterCodeDTO> starterCodeDTOs = problem.getStarterCodes().stream()
                .map(this::convertStarterCodeToDTO)
                .collect(Collectors.toList());
            dto.setStarterCodes(starterCodeDTOs);
        }

        return dto;
    }

    private TestCaseDTO convertTestCaseToDTO(TestCase testCase) {
        TestCaseDTO dto = new TestCaseDTO();
        dto.setId(testCase.getId());
        dto.setTestNumber(testCase.getTestNumber());
        dto.setInput(testCase.getInput());
        dto.setExpectedOutput(testCase.getExpectedOutput());
        dto.setIsSample(testCase.getIsSample());
        dto.setExplanation(testCase.getExplanation());
        dto.setTimeLimit(testCase.getTimeLimit());
        dto.setMemoryLimit(testCase.getMemoryLimit());
        return dto;
    }

    private StarterCodeDTO convertStarterCodeToDTO(StarterCode starterCode) {
        StarterCodeDTO dto = new StarterCodeDTO();
        dto.setId(starterCode.getId());
        dto.setLanguage(starterCode.getLanguage());
        dto.setCode(starterCode.getCode());
        return dto;
    }
}
