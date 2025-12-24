-- Seed initial data for Algorithm Visualizer

-- Insert algorithm categories for DSA
INSERT INTO alg_categories (name, slug, description, order_index) VALUES
('Sorting Algorithms', 'sorting', 'Fundamental sorting techniques', 1),
('Searching Algorithms', 'searching', 'Efficient searching methods', 2),
('Data Structures', 'data-structures', 'Core data structure implementations', 3),
('Graph Algorithms', 'graph-algorithms', 'Graph traversal and pathfinding', 4);

-- Insert algorithm categories for DAA  
INSERT INTO alg_categories (name, slug, description, order_index) VALUES
('Dynamic Programming', 'dynamic-programming', 'Optimization using dynamic programming', 5),
('Greedy Algorithms', 'greedy', 'Greedy approach algorithms', 6),
('Divide & Conquer', 'divide-conquer', 'Divide and conquer strategies', 7),
('Backtracking', 'backtracking', 'Backtracking algorithms', 8);

-- Insert DSA algorithms - Sorting (3 algorithms)
INSERT INTO algorithms (category_id, name, slug, type, difficulty, description) VALUES
(1, 'Bubble Sort', 'bubble-sort', 'SORTING', 'easy', 'Simple comparison-based sorting algorithm that repeatedly steps through the list.'),
(1, 'Quick Sort', 'quick-sort', 'SORTING', 'medium', 'Efficient divide-and-conquer sorting algorithm using pivot partitioning.'),
(1, 'Merge Sort', 'merge-sort', 'SORTING', 'medium', 'Stable divide-and-conquer sorting algorithm that merges sorted subarrays.');

-- Insert DSA algorithms - Searching (3 algorithms)
INSERT INTO algorithms (category_id, name, slug, type, difficulty, description) VALUES
(2, 'Linear Search', 'linear-search', 'SEARCHING', 'easy', 'Sequential search through array elements one by one.'),
(2, 'Binary Search', 'binary-search', 'SEARCHING', 'easy', 'Efficient search in sorted arrays using divide and conquer.'),
(2, 'Jump Search', 'jump-search', 'SEARCHING', 'medium', 'Search algorithm that jumps ahead by fixed steps then performs linear search.');

-- Insert DSA algorithms - Data Structures (3 algorithms)
INSERT INTO algorithms (category_id, name, slug, type, difficulty, description) VALUES
(3, 'Stack Implementation', 'stack-implementation', 'DATA_STRUCTURE', 'easy', 'Last-In-First-Out (LIFO) data structure with push, pop operations.'),
(3, 'Queue Implementation', 'queue-implementation', 'DATA_STRUCTURE', 'easy', 'First-In-First-Out (FIFO) data structure with enqueue, dequeue operations.'),
(3, 'Linked List Operations', 'linked-list-operations', 'DATA_STRUCTURE', 'medium', 'Dynamic data structure with insertion, deletion, traversal operations.');

-- Insert DSA algorithms - Graph Algorithms (3 algorithms)
INSERT INTO algorithms (category_id, name, slug, type, difficulty, description) VALUES
(4, 'Breadth-First Search', 'breadth-first-search', 'GRAPH', 'medium', 'Graph traversal algorithm exploring neighbors level by level.'),
(4, 'Depth-First Search', 'depth-first-search', 'GRAPH', 'medium', 'Graph traversal algorithm exploring as far as possible along each branch.'),
(4, 'Dijkstra Algorithm', 'dijkstra-algorithm', 'GRAPH', 'hard', 'Shortest path algorithm for weighted graphs with non-negative edges.');

-- Insert DAA algorithms - Dynamic Programming (3 algorithms)
INSERT INTO algorithms (category_id, name, slug, type, difficulty, description) VALUES
(5, '0/1 Knapsack', 'knapsack-01', 'DYNAMIC_PROGRAMMING', 'hard', 'Optimization problem to maximize value within weight constraint.'),
(5, 'Longest Common Subsequence', 'lcs', 'DYNAMIC_PROGRAMMING', 'medium', 'Finding longest subsequence common to two sequences.'),
(5, 'Fibonacci Sequence', 'fibonacci-dp', 'DYNAMIC_PROGRAMMING', 'easy', 'Computing Fibonacci numbers using dynamic programming approach.');

-- Insert DAA algorithms - Greedy (3 algorithms)
INSERT INTO algorithms (category_id, name, slug, type, difficulty, description) VALUES
(6, 'Activity Selection', 'activity-selection', 'GREEDY', 'medium', 'Selecting maximum number of non-overlapping activities.'),
(6, 'Huffman Coding', 'huffman-coding', 'GREEDY', 'hard', 'Data compression algorithm using variable-length codes.'),
(6, 'Fractional Knapsack', 'fractional-knapsack', 'GREEDY', 'medium', 'Knapsack variant allowing fractional items for maximum value.');

-- Insert DAA algorithms - Divide & Conquer (3 algorithms)
INSERT INTO algorithms (category_id, name, slug, type, difficulty, description) VALUES
(7, 'Binary Search Recursive', 'binary-search-recursive', 'DIVIDE_CONQUER', 'easy', 'Recursive implementation of binary search algorithm.'),
(7, 'Maximum Subarray', 'maximum-subarray', 'DIVIDE_CONQUER', 'medium', 'Finding contiguous subarray with largest sum using divide and conquer.'),
(7, 'Strassen Matrix Multiplication', 'strassen-matrix', 'DIVIDE_CONQUER', 'hard', 'Efficient matrix multiplication using divide and conquer approach.');

-- Insert DAA algorithms - Backtracking (3 algorithms)
INSERT INTO algorithms (category_id, name, slug, type, difficulty, description) VALUES
(8, 'N-Queens Problem', 'n-queens', 'BACKTRACKING', 'hard', 'Placing N queens on NxN chessboard so none attack each other.'),
(8, 'Sudoku Solver', 'sudoku-solver', 'BACKTRACKING', 'hard', 'Solving 9x9 Sudoku puzzle using backtracking approach.'),
(8, 'Subset Sum', 'subset-sum', 'BACKTRACKING', 'medium', 'Finding subset of numbers that sum to target value.');

-- Insert algorithm content for Merge Sort
INSERT INTO algorithm_content (algorithm_id, time_complexity, space_complexity, real_life_example, pseudocode, code_py, code_java, code_cpp, visualization_steps) VALUES
(1, 'O(n log n)', 'O(n)', 
'Like organizing a deck of cards by splitting it into smaller piles, sorting each pile, then merging them back together in order.',
'ALGORITHM MergeSort(arr, left, right)
  IF left < right THEN
    mid = (left + right) / 2
    MergeSort(arr, left, mid)
    MergeSort(arr, mid + 1, right)
    Merge(arr, left, mid, right)
  END IF
END ALGORITHM',
'def merge_sort(arr):
    if len(arr) <= 1:
        return arr
    
    mid = len(arr) // 2
    left = merge_sort(arr[:mid])
    right = merge_sort(arr[mid:])
    
    return merge(left, right)

def merge(left, right):
    result = []
    i = j = 0
    
    while i < len(left) and j < len(right):
        if left[i] <= right[j]:
            result.append(left[i])
            i += 1
        else:
            result.append(right[j])
            j += 1
    
    result.extend(left[i:])
    result.extend(right[j:])
    return result',
'public class MergeSort {
    public static void mergeSort(int[] arr, int left, int right) {
        if (left < right) {
            int mid = left + (right - left) / 2;
            
            mergeSort(arr, left, mid);
            mergeSort(arr, mid + 1, right);
            
            merge(arr, left, mid, right);
        }
    }
    
    private static void merge(int[] arr, int left, int mid, int right) {
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
    }
}',
'#include <vector>
using namespace std;

void merge(vector<int>& arr, int left, int mid, int right) {
    vector<int> temp(right - left + 1);
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
}

void mergeSort(vector<int>& arr, int left, int right) {
    if (left < right) {
        int mid = left + (right - left) / 2;
        mergeSort(arr, left, mid);
        mergeSort(arr, mid + 1, right);
        merge(arr, left, mid, right);
    }
}',
'[]');

-- Insert algorithm content for Dijkstra Algorithm
INSERT INTO algorithm_content (algorithm_id, time_complexity, space_complexity, real_life_example, pseudocode, code_py, code_java, code_cpp, visualization_steps) VALUES
(6, 'O((V + E) log V)', 'O(V)', 
'Like finding the shortest route on GPS navigation - it explores all possible paths and keeps track of the shortest distance to each destination.',
'ALGORITHM Dijkstra(graph, source)
  distance[source] = 0
  FOR each vertex v in graph
    IF v ≠ source THEN distance[v] = ∞
    previous[v] = undefined
    add v to Q
  END FOR
  
  WHILE Q is not empty
    u = vertex in Q with minimum distance
    remove u from Q
    FOR each neighbor v of u
      alt = distance[u] + weight(u, v)
      IF alt < distance[v] THEN
        distance[v] = alt
        previous[v] = u
      END IF
    END FOR
  END WHILE
END ALGORITHM',
'import heapq

def dijkstra(graph, start):
    distances = {node: float(''inf'') for node in graph}
    distances[start] = 0
    pq = [(0, start)]
    previous = {}
    
    while pq:
        current_distance, current = heapq.heappop(pq)
        
        if current_distance > distances[current]:
            continue
            
        for neighbor, weight in graph[current].items():
            distance = current_distance + weight
            
            if distance < distances[neighbor]:
                distances[neighbor] = distance
                previous[neighbor] = current
                heapq.heappush(pq, (distance, neighbor))
    
    return distances, previous',
'import java.util.*;

public class Dijkstra {
    public Map<String, Integer> dijkstra(Map<String, Map<String, Integer>> graph, String start) {
        Map<String, Integer> distances = new HashMap<>();
        Map<String, String> previous = new HashMap<>();
        PriorityQueue<Node> pq = new PriorityQueue<>();
        
        for (String node : graph.keySet()) {
            distances.put(node, Integer.MAX_VALUE);
        }
        distances.put(start, 0);
        pq.offer(new Node(start, 0));
        
        while (!pq.isEmpty()) {
            Node current = pq.poll();
            
            if (current.distance > distances.get(current.name)) {
                continue;
            }
            
            for (Map.Entry<String, Integer> neighbor : graph.get(current.name).entrySet()) {
                int distance = current.distance + neighbor.getValue();
                
                if (distance < distances.get(neighbor.getKey())) {
                    distances.put(neighbor.getKey(), distance);
                    previous.put(neighbor.getKey(), current.name);
                    pq.offer(new Node(neighbor.getKey(), distance));
                }
            }
        }
        
        return distances;
    }
    
    class Node implements Comparable<Node> {
        String name;
        int distance;
        
        Node(String name, int distance) {
            this.name = name;
            this.distance = distance;
        }
        
        public int compareTo(Node other) {
            return Integer.compare(this.distance, other.distance);
        }
    }
}',
'#include <vector>
#include <queue>
#include <unordered_map>
#include <climits>
using namespace std;

vector<int> dijkstra(vector<vector<pair<int, int>>>& graph, int start) {
    int n = graph.size();
    vector<int> distances(n, INT_MAX);
    priority_queue<pair<int, int>, vector<pair<int, int>>, greater<pair<int, int>>> pq;
    
    distances[start] = 0;
    pq.push({0, start});
    
    while (!pq.empty()) {
        int current_distance = pq.top().first;
        int current = pq.top().second;
        pq.pop();
        
        if (current_distance > distances[current]) {
            continue;
        }
        
        for (auto& edge : graph[current]) {
            int neighbor = edge.first;
            int weight = edge.second;
            int distance = current_distance + weight;
            
            if (distance < distances[neighbor]) {
                distances[neighbor] = distance;
                pq.push({distance, neighbor});
            }
        }
    }
    
    return distances;
}',
'[]');

-- Insert badges
INSERT INTO badges (name, description, threshold, tier, icon_url) VALUES
('First Steps', 'Complete your first algorithm visualization', 1, 'PASS', '/icons/first-steps.svg'),
('Quick Learner', 'Score 70% or higher on your first quiz', 70, 'PASS', '/icons/quick-learner.svg'),
('Silver Achiever', 'Score 80% or higher on any quiz', 80, 'SILVER', '/icons/silver-achiever.svg'),
('Gold Master', 'Score 90% or higher on any quiz', 90, 'GOLD', '/icons/gold-master.svg'),
('Algorithm Explorer', 'View 5 different algorithms', 5, 'PASS', '/icons/explorer.svg'),
('Sorting Specialist', 'Complete all sorting algorithms', 100, 'SILVER', '/icons/sorting-specialist.svg'),
('Graph Guru', 'Master all graph algorithms', 100, 'GOLD', '/icons/graph-guru.svg');

-- Insert sample quizzes for Merge Sort
INSERT INTO quizzes (algorithm_id, title, difficulty, num_questions) VALUES
(1, 'Merge Sort Quiz', 'medium', 5);

-- Insert quiz questions for Merge Sort
INSERT INTO quiz_questions (quiz_id, question_text, options_json, correct_option_index, explanation_text, order_index) VALUES
(1, 'What is the time complexity of Merge Sort?', 
'["O(n)", "O(n log n)", "O(n²)", "O(log n)"]', 
1, 
'Merge Sort has a time complexity of O(n log n) because it divides the array into halves (log n levels) and merges them (n operations per level).', 
1),

(1, 'What is the space complexity of Merge Sort?', 
'["O(1)", "O(log n)", "O(n)", "O(n log n)"]', 
2, 
'Merge Sort requires O(n) extra space for the temporary arrays used during the merge process.', 
2),

(1, 'Merge Sort is a stable sorting algorithm. What does this mean?', 
'["It always runs in the same time", "It uses constant extra space", "It preserves the relative order of equal elements", "It works only on sorted arrays"]', 
2, 
'A stable sorting algorithm preserves the relative order of equal elements in the sorted output.', 
3),

(1, 'Which approach does Merge Sort use?', 
'["Greedy", "Dynamic Programming", "Divide and Conquer", "Backtracking"]', 
2, 
'Merge Sort uses the divide and conquer approach by recursively dividing the array and then merging the sorted halves.', 
4),

(1, 'In the worst case, how many comparisons does Merge Sort make for an array of size n?', 
'["n", "n log n", "n²", "2n"]', 
1, 
'In the worst case, Merge Sort makes approximately n log n comparisons.', 
5);

-- Insert sample exercises
INSERT INTO exercises (algorithm_id, title, description, difficulty, level_number, starter_code_py, starter_code_java, starter_code_cpp, test_cases, expected_output, hints) VALUES
(1, 'Implement Merge Function', 
'Implement the merge function that combines two sorted arrays into one sorted array.', 
'easy', 
1,
'def merge(left, right):
    # Your code here
    pass',
'public static int[] merge(int[] left, int[] right) {
    // Your code here
    return new int[0];
}',
'vector<int> merge(vector<int>& left, vector<int>& right) {
    // Your code here
    return {};
}',
'[{"input": "left=[1,3,5], right=[2,4,6]", "expected": "[1,2,3,4,5,6]"}]',
'[1,2,3,4,5,6]',
'["Use two pointers to compare elements", "Always take the smaller element first", "Don''t forget to add remaining elements"]');

-- Create admin user (password: admin123)
INSERT INTO users (email, password_hash, display_name, role) VALUES
('admin@algovisualizer.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'Administrator', 'ADMIN');
