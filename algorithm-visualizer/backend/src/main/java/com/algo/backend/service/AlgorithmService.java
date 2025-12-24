package com.algo.backend.service;

import com.algo.backend.entity.Algorithm;
import com.algo.backend.entity.AlgorithmCategory;
import com.algo.backend.entity.AlgorithmContent;
import com.algo.backend.repository.AlgorithmRepository;
import com.algo.backend.repository.AlgorithmCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AlgorithmService {

    @Autowired
    private AlgorithmRepository algorithmRepository;
    
    @Autowired
    private AlgorithmCategoryRepository categoryRepository;

    public List<Algorithm> getAllAlgorithms() {
        return algorithmRepository.findAllOrderedByCategory();
    }

    public Optional<Algorithm> getAlgorithmById(Long id) {
        return algorithmRepository.findById(id);
    }

    public Optional<Algorithm> getAlgorithmByName(String name) {
        return algorithmRepository.findByName(name);
    }
    
    public Optional<Algorithm> getAlgorithmBySlug(String slug) {
        return algorithmRepository.findBySlug(slug);
    }

    public List<Algorithm> getAlgorithmsByCategory(AlgorithmCategory category) {
        return algorithmRepository.findByCategoryOrderByName(category);
    }

    public List<Algorithm> getAlgorithmsByDifficulty(Algorithm.Difficulty difficulty) {
        return algorithmRepository.findByDifficulty(difficulty);
    }

    public List<AlgorithmCategory> getAllCategories() {
        return categoryRepository.findAllOrderedByIndex();
    }

    public List<Algorithm> getRandomAlgorithms(int limit) {
        return algorithmRepository.findRandomAlgorithms(limit);
    }

    public Algorithm saveAlgorithm(Algorithm algorithm) {
        return algorithmRepository.save(algorithm);
    }

    public void deleteAlgorithm(Long id) {
        algorithmRepository.deleteById(id);
    }

    public Algorithm updateAlgorithm(Long id, Algorithm algorithmDetails) {
        Optional<Algorithm> optionalAlgorithm = algorithmRepository.findById(id);
        if (optionalAlgorithm.isPresent()) {
            Algorithm algorithm = optionalAlgorithm.get();
            algorithm.setName(algorithmDetails.getName());
            algorithm.setDescription(algorithmDetails.getDescription());
            algorithm.setCategory(algorithmDetails.getCategory());
            algorithm.setDifficulty(algorithmDetails.getDifficulty());
            algorithm.setType(algorithmDetails.getType());
            return algorithmRepository.save(algorithm);
        }
        return null;
    }
    
    public AlgorithmContent getAlgorithmContent(Long algorithmId) {
        Optional<Algorithm> algorithm = algorithmRepository.findById(algorithmId);
        return algorithm.map(Algorithm::getContent).orElse(null);
    }
}
