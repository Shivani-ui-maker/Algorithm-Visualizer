package com.algo.backend.service;

import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class VisualizationService {

    /**
     * Generate visualization steps for sorting algorithms
     */
    public Map<String, Object> generateSortingVisualization(String algorithmName, int[] array) {
        Map<String, Object> result = new HashMap<>();
        List<Map<String, Object>> steps = new ArrayList<>();
        
        switch (algorithmName.toLowerCase()) {
            case "bubble-sort":
                steps = bubbleSortVisualization(array.clone());
                break;
            case "selection-sort":
                steps = selectionSortVisualization(array.clone());
                break;
            case "insertion-sort":
                steps = insertionSortVisualization(array.clone());
                break;
            case "merge-sort":
                steps = mergeSortVisualization(array.clone(), 0, array.length - 1);
                break;
            case "quick-sort":
                steps = quickSortVisualization(array.clone(), 0, array.length - 1);
                break;
            case "heap-sort":
                steps = heapSortVisualization(array.clone());
                break;
            default:
                throw new IllegalArgumentException("Unsupported sorting algorithm: " + algorithmName);
        }
        
        result.put("algorithm", algorithmName);
        result.put("originalArray", array);
        result.put("steps", steps);
        result.put("totalSteps", steps.size());
        
        return result;
    }

    /**
     * Generate visualization steps for searching algorithms
     */
    public Map<String, Object> generateSearchVisualization(String algorithmName, int[] array, int target) {
        Map<String, Object> result = new HashMap<>();
        List<Map<String, Object>> steps = new ArrayList<>();
        
        switch (algorithmName.toLowerCase()) {
            case "linear-search":
                steps = linearSearchVisualization(array, target);
                break;
            case "binary-search":
                steps = binarySearchVisualization(array, target);
                break;
            case "jump-search":
                steps = jumpSearchVisualization(array, target);
                break;
            default:
                throw new IllegalArgumentException("Unsupported search algorithm: " + algorithmName);
        }
        
        result.put("algorithm", algorithmName);
        result.put("array", array);
        result.put("target", target);
        result.put("steps", steps);
        result.put("totalSteps", steps.size());
        
        return result;
    }

    /**
     * Generate visualization steps for graph algorithms
     */
    public Map<String, Object> generateGraphVisualization(String algorithmName, Map<String, Object> graphData) {
        Map<String, Object> result = new HashMap<>();
        List<Map<String, Object>> steps = new ArrayList<>();
        
        switch (algorithmName.toLowerCase()) {
            case "bfs":
                steps = bfsVisualization(graphData);
                break;
            case "dfs":
                steps = dfsVisualization(graphData);
                break;
            case "dijkstra":
                steps = dijkstraVisualization(graphData);
                break;
            default:
                throw new IllegalArgumentException("Unsupported graph algorithm: " + algorithmName);
        }
        
        result.put("algorithm", algorithmName);
        result.put("graph", graphData);
        result.put("steps", steps);
        result.put("totalSteps", steps.size());
        
        return result;
    }

    // Bubble Sort Visualization
    private List<Map<String, Object>> bubbleSortVisualization(int[] arr) {
        List<Map<String, Object>> steps = new ArrayList<>();
        int n = arr.length;
        
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                // Add comparison step
                Map<String, Object> compareStep = new HashMap<>();
                compareStep.put("type", "compare");
                compareStep.put("array", arr.clone());
                compareStep.put("comparing", Arrays.asList(j, j + 1));
                compareStep.put("description", "Comparing elements at positions " + j + " and " + (j + 1));
                steps.add(compareStep);
                
                if (arr[j] > arr[j + 1]) {
                    // Add swap step
                    Map<String, Object> swapStep = new HashMap<>();
                    swapStep.put("type", "swap");
                    swapStep.put("array", arr.clone());
                    swapStep.put("swapping", Arrays.asList(j, j + 1));
                    swapStep.put("description", "Swapping elements at positions " + j + " and " + (j + 1));
                    steps.add(swapStep);
                    
                    // Perform swap
                    int temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                    
                    // Add result step
                    Map<String, Object> resultStep = new HashMap<>();
                    resultStep.put("type", "result");
                    resultStep.put("array", arr.clone());
                    resultStep.put("description", "Array after swap");
                    steps.add(resultStep);
                }
            }
            
            // Mark element as sorted
            Map<String, Object> sortedStep = new HashMap<>();
            sortedStep.put("type", "sorted");
            sortedStep.put("array", arr.clone());
            sortedStep.put("sortedIndex", n - i - 1);
            sortedStep.put("description", "Element at position " + (n - i - 1) + " is now in its correct position");
            steps.add(sortedStep);
        }
        
        return steps;
    }

    // Selection Sort Visualization
    private List<Map<String, Object>> selectionSortVisualization(int[] arr) {
        List<Map<String, Object>> steps = new ArrayList<>();
        int n = arr.length;
        
        for (int i = 0; i < n - 1; i++) {
            int minIdx = i;
            
            // Find minimum element
            for (int j = i + 1; j < n; j++) {
                Map<String, Object> compareStep = new HashMap<>();
                compareStep.put("type", "compare");
                compareStep.put("array", arr.clone());
                compareStep.put("comparing", Arrays.asList(minIdx, j));
                compareStep.put("currentMin", minIdx);
                compareStep.put("description", "Comparing with current minimum");
                steps.add(compareStep);
                
                if (arr[j] < arr[minIdx]) {
                    minIdx = j;
                    Map<String, Object> newMinStep = new HashMap<>();
                    newMinStep.put("type", "newMin");
                    newMinStep.put("array", arr.clone());
                    newMinStep.put("newMinIndex", minIdx);
                    newMinStep.put("description", "New minimum found at position " + minIdx);
                    steps.add(newMinStep);
                }
            }
            
            // Swap if needed
            if (minIdx != i) {
                Map<String, Object> swapStep = new HashMap<>();
                swapStep.put("type", "swap");
                swapStep.put("array", arr.clone());
                swapStep.put("swapping", Arrays.asList(i, minIdx));
                swapStep.put("description", "Swapping minimum element to position " + i);
                steps.add(swapStep);
                
                int temp = arr[minIdx];
                arr[minIdx] = arr[i];
                arr[i] = temp;
            }
            
            Map<String, Object> sortedStep = new HashMap<>();
            sortedStep.put("type", "sorted");
            sortedStep.put("array", arr.clone());
            sortedStep.put("sortedIndex", i);
            sortedStep.put("description", "Element at position " + i + " is now sorted");
            steps.add(sortedStep);
        }
        
        return steps;
    }

    // Insertion Sort Visualization
    private List<Map<String, Object>> insertionSortVisualization(int[] arr) {
        List<Map<String, Object>> steps = new ArrayList<>();
        
        for (int i = 1; i < arr.length; i++) {
            int key = arr[i];
            int j = i - 1;
            
            Map<String, Object> selectStep = new HashMap<>();
            selectStep.put("type", "select");
            selectStep.put("array", arr.clone());
            selectStep.put("selectedIndex", i);
            selectStep.put("key", key);
            selectStep.put("description", "Selecting element " + key + " to insert");
            steps.add(selectStep);
            
            while (j >= 0 && arr[j] > key) {
                Map<String, Object> compareStep = new HashMap<>();
                compareStep.put("type", "compare");
                compareStep.put("array", arr.clone());
                compareStep.put("comparing", Arrays.asList(j, i));
                compareStep.put("description", "Comparing " + arr[j] + " with " + key);
                steps.add(compareStep);
                
                arr[j + 1] = arr[j];
                
                Map<String, Object> shiftStep = new HashMap<>();
                shiftStep.put("type", "shift");
                shiftStep.put("array", arr.clone());
                shiftStep.put("shiftedIndex", j);
                shiftStep.put("description", "Shifting element to the right");
                steps.add(shiftStep);
                
                j = j - 1;
            }
            
            arr[j + 1] = key;
            
            Map<String, Object> insertStep = new HashMap<>();
            insertStep.put("type", "insert");
            insertStep.put("array", arr.clone());
            insertStep.put("insertedIndex", j + 1);
            insertStep.put("description", "Inserting " + key + " at position " + (j + 1));
            steps.add(insertStep);
        }
        
        return steps;
    }

    // Merge Sort Visualization (simplified)
    private List<Map<String, Object>> mergeSortVisualization(int[] arr, int left, int right) {
        List<Map<String, Object>> steps = new ArrayList<>();
        mergeSortHelper(arr, left, right, steps);
        return steps;
    }

    private void mergeSortHelper(int[] arr, int left, int right, List<Map<String, Object>> steps) {
        if (left < right) {
            int mid = left + (right - left) / 2;
            
            Map<String, Object> divideStep = new HashMap<>();
            divideStep.put("type", "divide");
            divideStep.put("array", arr.clone());
            divideStep.put("left", left);
            divideStep.put("mid", mid);
            divideStep.put("right", right);
            divideStep.put("description", "Dividing array from " + left + " to " + right);
            steps.add(divideStep);
            
            mergeSortHelper(arr, left, mid, steps);
            mergeSortHelper(arr, mid + 1, right, steps);
            merge(arr, left, mid, right, steps);
        }
    }

    private void merge(int[] arr, int left, int mid, int right, List<Map<String, Object>> steps) {
        int[] temp = new int[right - left + 1];
        int i = left, j = mid + 1, k = 0;
        
        while (i <= mid && j <= right) {
            if (arr[i] <= arr[j]) {
                temp[k++] = arr[i++];
            } else {
                temp[k++] = arr[j++];
            }
        }
        
        while (i <= mid) temp[k++] = arr[i++];
        while (j <= right) temp[k++] = arr[j++];
        
        for (i = left; i <= right; i++) {
            arr[i] = temp[i - left];
        }
        
        Map<String, Object> mergeStep = new HashMap<>();
        mergeStep.put("type", "merge");
        mergeStep.put("array", arr.clone());
        mergeStep.put("left", left);
        mergeStep.put("right", right);
        mergeStep.put("description", "Merging subarrays from " + left + " to " + right);
        steps.add(mergeStep);
    }

    // Quick Sort Visualization (simplified)
    private List<Map<String, Object>> quickSortVisualization(int[] arr, int low, int high) {
        List<Map<String, Object>> steps = new ArrayList<>();
        quickSortHelper(arr, low, high, steps);
        return steps;
    }

    private void quickSortHelper(int[] arr, int low, int high, List<Map<String, Object>> steps) {
        if (low < high) {
            int pi = partition(arr, low, high, steps);
            quickSortHelper(arr, low, pi - 1, steps);
            quickSortHelper(arr, pi + 1, high, steps);
        }
    }

    private int partition(int[] arr, int low, int high, List<Map<String, Object>> steps) {
        int pivot = arr[high];
        int i = (low - 1);
        
        Map<String, Object> pivotStep = new HashMap<>();
        pivotStep.put("type", "pivot");
        pivotStep.put("array", arr.clone());
        pivotStep.put("pivotIndex", high);
        pivotStep.put("description", "Choosing pivot: " + pivot);
        steps.add(pivotStep);
        
        for (int j = low; j < high; j++) {
            if (arr[j] < pivot) {
                i++;
                int temp = arr[i];
                arr[i] = arr[j];
                arr[j] = temp;
                
                Map<String, Object> swapStep = new HashMap<>();
                swapStep.put("type", "swap");
                swapStep.put("array", arr.clone());
                swapStep.put("swapping", Arrays.asList(i, j));
                swapStep.put("description", "Swapping elements smaller than pivot");
                steps.add(swapStep);
            }
        }
        
        int temp = arr[i + 1];
        arr[i + 1] = arr[high];
        arr[high] = temp;
        
        Map<String, Object> finalSwapStep = new HashMap<>();
        finalSwapStep.put("type", "pivotPlace");
        finalSwapStep.put("array", arr.clone());
        finalSwapStep.put("pivotFinalIndex", i + 1);
        finalSwapStep.put("description", "Placing pivot in correct position");
        steps.add(finalSwapStep);
        
        return i + 1;
    }

    // Heap Sort Visualization (simplified)
    private List<Map<String, Object>> heapSortVisualization(int[] arr) {
        List<Map<String, Object>> steps = new ArrayList<>();
        int n = arr.length;
        
        // Build heap
        for (int i = n / 2 - 1; i >= 0; i--) {
            heapify(arr, n, i, steps);
        }
        
        // Extract elements from heap
        for (int i = n - 1; i > 0; i--) {
            int temp = arr[0];
            arr[0] = arr[i];
            arr[i] = temp;
            
            Map<String, Object> extractStep = new HashMap<>();
            extractStep.put("type", "extract");
            extractStep.put("array", arr.clone());
            extractStep.put("extractedIndex", i);
            extractStep.put("description", "Extracting maximum element");
            steps.add(extractStep);
            
            heapify(arr, i, 0, steps);
        }
        
        return steps;
    }

    private void heapify(int[] arr, int n, int i, List<Map<String, Object>> steps) {
        int largest = i;
        int left = 2 * i + 1;
        int right = 2 * i + 2;
        
        if (left < n && arr[left] > arr[largest]) largest = left;
        if (right < n && arr[right] > arr[largest]) largest = right;
        
        if (largest != i) {
            int swap = arr[i];
            arr[i] = arr[largest];
            arr[largest] = swap;
            
            Map<String, Object> heapifyStep = new HashMap<>();
            heapifyStep.put("type", "heapify");
            heapifyStep.put("array", arr.clone());
            heapifyStep.put("swapping", Arrays.asList(i, largest));
            heapifyStep.put("description", "Maintaining heap property");
            steps.add(heapifyStep);
            
            heapify(arr, n, largest, steps);
        }
    }

    // Linear Search Visualization
    private List<Map<String, Object>> linearSearchVisualization(int[] arr, int target) {
        List<Map<String, Object>> steps = new ArrayList<>();
        
        for (int i = 0; i < arr.length; i++) {
            Map<String, Object> step = new HashMap<>();
            step.put("type", "check");
            step.put("array", arr.clone());
            step.put("currentIndex", i);
            step.put("target", target);
            step.put("found", arr[i] == target);
            step.put("description", "Checking element at position " + i + ": " + arr[i]);
            steps.add(step);
            
            if (arr[i] == target) {
                Map<String, Object> foundStep = new HashMap<>();
                foundStep.put("type", "found");
                foundStep.put("array", arr.clone());
                foundStep.put("foundIndex", i);
                foundStep.put("target", target);
                foundStep.put("description", "Target " + target + " found at position " + i);
                steps.add(foundStep);
                break;
            }
        }
        
        return steps;
    }

    // Binary Search Visualization
    private List<Map<String, Object>> binarySearchVisualization(int[] arr, int target) {
        List<Map<String, Object>> steps = new ArrayList<>();
        int left = 0, right = arr.length - 1;
        
        while (left <= right) {
            int mid = left + (right - left) / 2;
            
            Map<String, Object> step = new HashMap<>();
            step.put("type", "check");
            step.put("array", arr.clone());
            step.put("left", left);
            step.put("right", right);
            step.put("mid", mid);
            step.put("target", target);
            step.put("midValue", arr[mid]);
            step.put("description", "Checking middle element: " + arr[mid]);
            steps.add(step);
            
            if (arr[mid] == target) {
                Map<String, Object> foundStep = new HashMap<>();
                foundStep.put("type", "found");
                foundStep.put("array", arr.clone());
                foundStep.put("foundIndex", mid);
                foundStep.put("target", target);
                foundStep.put("description", "Target " + target + " found at position " + mid);
                steps.add(foundStep);
                break;
            }
            
            if (arr[mid] < target) {
                left = mid + 1;
                Map<String, Object> moveStep = new HashMap<>();
                moveStep.put("type", "moveRight");
                moveStep.put("array", arr.clone());
                moveStep.put("newLeft", left);
                moveStep.put("description", "Target is greater, searching right half");
                steps.add(moveStep);
            } else {
                right = mid - 1;
                Map<String, Object> moveStep = new HashMap<>();
                moveStep.put("type", "moveLeft");
                moveStep.put("array", arr.clone());
                moveStep.put("newRight", right);
                moveStep.put("description", "Target is smaller, searching left half");
                steps.add(moveStep);
            }
        }
        
        return steps;
    }

    // Jump Search Visualization
    private List<Map<String, Object>> jumpSearchVisualization(int[] arr, int target) {
        List<Map<String, Object>> steps = new ArrayList<>();
        int n = arr.length;
        int step = (int) Math.floor(Math.sqrt(n));
        int prev = 0;
        
        // Jump search
        while (arr[Math.min(step, n) - 1] < target) {
            Map<String, Object> jumpStep = new HashMap<>();
            jumpStep.put("type", "jump");
            jumpStep.put("array", arr.clone());
            jumpStep.put("currentStep", step);
            jumpStep.put("target", target);
            jumpStep.put("description", "Jumping to position " + step);
            steps.add(jumpStep);
            
            prev = step;
            step += (int) Math.floor(Math.sqrt(n));
            if (prev >= n) break;
        }
        
        // Linear search in the identified block
        while (arr[prev] < target) {
            Map<String, Object> linearStep = new HashMap<>();
            linearStep.put("type", "linearCheck");
            linearStep.put("array", arr.clone());
            linearStep.put("currentIndex", prev);
            linearStep.put("target", target);
            linearStep.put("description", "Linear search in block, checking position " + prev);
            steps.add(linearStep);
            
            prev++;
            if (prev == Math.min(step, n)) break;
        }
        
        if (prev < n && arr[prev] == target) {
            Map<String, Object> foundStep = new HashMap<>();
            foundStep.put("type", "found");
            foundStep.put("array", arr.clone());
            foundStep.put("foundIndex", prev);
            foundStep.put("target", target);
            foundStep.put("description", "Target " + target + " found at position " + prev);
            steps.add(foundStep);
        }
        
        return steps;
    }

    // BFS Visualization (simplified)
    private List<Map<String, Object>> bfsVisualization(Map<String, Object> graphData) {
        List<Map<String, Object>> steps = new ArrayList<>();
        // Implementation would depend on graph structure
        // This is a placeholder for BFS visualization
        Map<String, Object> step = new HashMap<>();
        step.put("type", "bfs");
        step.put("description", "BFS traversal visualization");
        steps.add(step);
        return steps;
    }

    // DFS Visualization (simplified)
    private List<Map<String, Object>> dfsVisualization(Map<String, Object> graphData) {
        List<Map<String, Object>> steps = new ArrayList<>();
        // Implementation would depend on graph structure
        // This is a placeholder for DFS visualization
        Map<String, Object> step = new HashMap<>();
        step.put("type", "dfs");
        step.put("description", "DFS traversal visualization");
        steps.add(step);
        return steps;
    }

    // Dijkstra Visualization (simplified)
    private List<Map<String, Object>> dijkstraVisualization(Map<String, Object> graphData) {
        List<Map<String, Object>> steps = new ArrayList<>();
        // Implementation would depend on graph structure
        // This is a placeholder for Dijkstra visualization
        Map<String, Object> step = new HashMap<>();
        step.put("type", "dijkstra");
        step.put("description", "Dijkstra shortest path visualization");
        steps.add(step);
        return steps;
    }
}
