package com.algo.backend.service;

import com.algo.backend.entity.*;
import com.algo.backend.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class DataInitializationService {
    // Temporarily disabled CommandLineRunner to prevent startup errors

    @Autowired
    private AlgorithmCategoryRepository categoryRepository;
    
    @Autowired
    private AlgorithmRepository algorithmRepository;
    
    @Autowired
    private BadgeRepository badgeRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    // @Override
    @Transactional
    public void run(String... args) throws Exception {
        initializeData();
    }

    private void initializeData() {
        // Check if data already exists
        if (categoryRepository.count() > 0) {
            return; // Data already initialized
        }

        // Create algorithm categories
        AlgorithmCategory dsaCategory = new AlgorithmCategory();
        dsaCategory.setName("Data Structures & Algorithms (DSA)");
        dsaCategory.setSlug("dsa");
        dsaCategory.setDescription("Fundamental algorithms and data structures");
        dsaCategory.setOrderIndex(1);
        dsaCategory = categoryRepository.save(dsaCategory);

        AlgorithmCategory daaCategory = new AlgorithmCategory();
        daaCategory.setName("Design & Analysis of Algorithms (DAA)");
        daaCategory.setSlug("daa");
        daaCategory.setDescription("Advanced algorithm design and analysis techniques");
        daaCategory.setOrderIndex(2);
        daaCategory = categoryRepository.save(daaCategory);

        // Create algorithms
        Algorithm mergeSort = new Algorithm();
        mergeSort.setCategory(dsaCategory);
        mergeSort.setName("Merge Sort");
        mergeSort.setSlug("merge-sort");
        mergeSort.setType(Algorithm.AlgorithmType.ARRAY);
        mergeSort.setDifficulty(Algorithm.Difficulty.MEDIUM);
        mergeSort.setDescription("A divide-and-conquer sorting algorithm that divides the array into halves, sorts them, and merges them back together.");
        algorithmRepository.save(mergeSort);

        Algorithm bubbleSort = new Algorithm();
        bubbleSort.setCategory(dsaCategory);
        bubbleSort.setName("Bubble Sort");
        bubbleSort.setSlug("bubble-sort");
        bubbleSort.setType(Algorithm.AlgorithmType.ARRAY);
        bubbleSort.setDifficulty(Algorithm.Difficulty.EASY);
        bubbleSort.setDescription("A simple sorting algorithm that repeatedly steps through the list, compares adjacent elements and swaps them if they are in the wrong order.");
        algorithmRepository.save(bubbleSort);

        Algorithm quickSort = new Algorithm();
        quickSort.setCategory(dsaCategory);
        quickSort.setName("Quick Sort");
        quickSort.setSlug("quick-sort");
        quickSort.setType(Algorithm.AlgorithmType.ARRAY);
        quickSort.setDifficulty(Algorithm.Difficulty.MEDIUM);
        quickSort.setDescription("An efficient sorting algorithm that uses divide-and-conquer to sort arrays by selecting a pivot element.");
        algorithmRepository.save(quickSort);

        Algorithm binarySearch = new Algorithm();
        binarySearch.setCategory(dsaCategory);
        binarySearch.setName("Binary Search");
        binarySearch.setSlug("binary-search");
        binarySearch.setType(Algorithm.AlgorithmType.ARRAY);
        binarySearch.setDifficulty(Algorithm.Difficulty.EASY);
        binarySearch.setDescription("An efficient algorithm for finding an item from a sorted list of items by repeatedly dividing the search interval in half.");
        algorithmRepository.save(binarySearch);

        Algorithm linkedList = new Algorithm();
        linkedList.setCategory(dsaCategory);
        linkedList.setName("Singly Linked List");
        linkedList.setSlug("singly-linked-list");
        linkedList.setType(Algorithm.AlgorithmType.LINKED_LIST);
        linkedList.setDifficulty(Algorithm.Difficulty.EASY);
        linkedList.setDescription("A linear data structure where elements are stored in nodes, each containing data and a pointer to the next node.");
        algorithmRepository.save(linkedList);

        Algorithm dijkstra = new Algorithm();
        dijkstra.setCategory(daaCategory);
        dijkstra.setName("Dijkstra Algorithm");
        dijkstra.setSlug("dijkstra-algorithm");
        dijkstra.setType(Algorithm.AlgorithmType.GRAPH);
        dijkstra.setDifficulty(Algorithm.Difficulty.HARD);
        dijkstra.setDescription("A graph algorithm that finds the shortest path between nodes in a weighted graph.");
        algorithmRepository.save(dijkstra);

        // Create badges
        Badge firstSteps = new Badge();
        firstSteps.setName("First Steps");
        firstSteps.setDescription("Complete your first algorithm visualization");
        firstSteps.setType(Badge.BadgeType.ALGORITHMS_COMPLETED);
        firstSteps.setRequiredValue(1);
        firstSteps.setIsActive(true);
        badgeRepository.save(firstSteps);

        Badge quickLearner = new Badge();
        quickLearner.setName("Quick Learner");
        quickLearner.setDescription("Score 70% or higher on your first quiz");
        quickLearner.setType(Badge.BadgeType.QUIZ_SCORE);
        quickLearner.setRequiredValue(70);
        quickLearner.setIsActive(true);
        badgeRepository.save(quickLearner);

        Badge silverAchiever = new Badge();
        silverAchiever.setName("Silver Achiever");
        silverAchiever.setDescription("Score 80% or higher on any quiz");
        silverAchiever.setType(Badge.BadgeType.QUIZ_SCORE);
        silverAchiever.setRequiredValue(80);
        silverAchiever.setIsActive(true);
        badgeRepository.save(silverAchiever);

        Badge goldMaster = new Badge();
        goldMaster.setName("Gold Master");
        goldMaster.setDescription("Score 90% or higher on any quiz");
        goldMaster.setType(Badge.BadgeType.QUIZ_SCORE);
        goldMaster.setRequiredValue(90);
        goldMaster.setIsActive(true);
        badgeRepository.save(goldMaster);

        // Create admin user
        User adminUser = new User();
        adminUser.setEmail("admin@algovisualizer.com");
        adminUser.setPasswordHash(passwordEncoder.encode("admin123"));
        adminUser.setDisplayName("Administrator");
        adminUser.setRole(User.Role.ADMIN);
        userRepository.save(adminUser);

        System.out.println("✅ Database initialized with sample data");
    }
}
