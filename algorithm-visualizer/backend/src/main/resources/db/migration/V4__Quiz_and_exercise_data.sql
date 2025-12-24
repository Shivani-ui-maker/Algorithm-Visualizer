-- Quiz and Exercise data for all algorithms

-- Quizzes for Sorting Algorithms
INSERT INTO quizzes (algorithm_id, title, difficulty, num_questions) VALUES
(1, 'Bubble Sort Quiz', 'easy', 4),
(2, 'Quick Sort Quiz', 'medium', 5),
(3, 'Merge Sort Quiz', 'medium', 5);

-- Quiz questions for Bubble Sort
INSERT INTO quiz_questions (quiz_id, question_text, options, correct_answer, explanation, order_index) VALUES
(1, 'What is the time complexity of Bubble Sort in the worst case?', '["O(n)", "O(n log n)", "O(n²)", "O(log n)"]', 2, 'Bubble Sort has O(n²) time complexity in worst case due to nested loops.', 1),
(1, 'Bubble Sort is a stable sorting algorithm.', '["True", "False"]', 0, 'Bubble Sort maintains relative order of equal elements, making it stable.', 2),
(1, 'In which case does Bubble Sort perform best?', '["Random array", "Sorted array", "Reverse sorted", "All same elements"]', 1, 'Bubble Sort performs best on already sorted arrays with O(n) complexity.', 3),
(1, 'What happens in each pass of Bubble Sort?', '["Smallest element moves to start", "Largest element moves to end", "Array gets reversed", "Nothing significant"]', 1, 'Each pass bubbles the largest unsorted element to its correct position at the end.', 4);

-- Quiz questions for Quick Sort
INSERT INTO quiz_questions (quiz_id, question_text, options, correct_answer, explanation, order_index) VALUES
(2, 'What is the average time complexity of Quick Sort?', '["O(n)", "O(n log n)", "O(n²)", "O(log n)"]', 1, 'Quick Sort has O(n log n) average time complexity.', 1),
(2, 'Quick Sort uses which algorithmic approach?', '["Divide and Conquer", "Dynamic Programming", "Greedy", "Backtracking"]', 0, 'Quick Sort divides array around pivot and conquers subproblems.', 2),
(2, 'What is the worst-case time complexity of Quick Sort?', '["O(n)", "O(n log n)", "O(n²)", "O(log n)"]', 2, 'Worst case occurs when pivot is always smallest or largest element.', 3),
(2, 'Quick Sort is an in-place sorting algorithm.', '["True", "False"]', 0, 'Quick Sort sorts the array without requiring additional space proportional to input size.', 4),
(2, 'Which factor most affects Quick Sort performance?', '["Array size", "Pivot selection", "Data type", "Memory available"]', 1, 'Pivot selection strategy significantly impacts Quick Sort performance.', 5);

-- Exercises for Sorting Algorithms
INSERT INTO exercises (algorithm_id, title, description, difficulty, question_type, options, correct_answer, level_number) VALUES
(1, 'Implement Bubble Sort', 'Write a function to sort an array using bubble sort algorithm. The function should take an array as input and return the sorted array.', 'easy', 'MCQ', '["Correct implementation with nested loops", "Missing swap operation", "Incorrect loop bounds", "No return statement"]', 'Correct implementation with nested loops', 1),
(2, 'Quick Sort Partition', 'Implement the partition function for Quick Sort that places pivot in correct position and returns its index.', 'medium', 'MCQ', '["Correct partition with proper pivot placement", "Incorrect pivot selection", "Missing element swapping", "Wrong return value"]', 'Correct partition with proper pivot placement', 1),
(3, 'Merge Function Implementation', 'Write the merge function that combines two sorted arrays into one sorted array for Merge Sort.', 'medium', 'MCQ', '["Proper merging of two sorted arrays", "Missing boundary checks", "Incorrect index management", "No handling of remaining elements"]', 'Proper merging of two sorted arrays', 1);

-- Exercises for Searching Algorithms
INSERT INTO exercises (algorithm_id, title, description, difficulty, question_type, options, correct_answer, level_number) VALUES
(4, 'Linear Search Implementation', 'Implement linear search to find target element in array. Return index if found, -1 otherwise.', 'easy', 'MCQ', '["Correct linear traversal with target check", "Missing return statement", "Incorrect loop condition", "Wrong return value"]', 'Correct linear traversal with target check', 1),
(5, 'Binary Search on Sorted Array', 'Implement binary search on sorted array. Handle edge cases properly.', 'easy', 'MCQ', '["Proper mid calculation and bounds update", "Integer overflow in mid calculation", "Incorrect boundary conditions", "Missing target comparison"]', 'Proper mid calculation and bounds update', 1),
(6, 'Jump Search Optimization', 'Implement jump search with optimal jump size for given array length.', 'medium', 'MCQ', '["Optimal jump size with linear search", "Incorrect jump size calculation", "Missing linear search phase", "Wrong boundary handling"]', 'Optimal jump size with linear search', 1);

-- Exercises for Data Structures
INSERT INTO exercises (algorithm_id, title, description, difficulty, question_type, options, correct_answer, level_number) VALUES
(7, 'Stack Push Operation', 'Implement push operation for stack. Handle overflow conditions.', 'easy', 'MCQ', '["Proper element addition with size check", "Missing overflow check", "Incorrect top pointer update", "No element validation"]', 'Proper element addition with size check', 1),
(8, 'Queue Enqueue Implementation', 'Implement enqueue operation for circular queue with proper wraparound.', 'easy', 'MCQ', '["Correct circular queue insertion", "Missing wraparound logic", "Incorrect rear pointer update", "No full queue check"]', 'Correct circular queue insertion', 1),
(9, 'Linked List Insertion', 'Implement insertion at beginning of singly linked list.', 'medium', 'MCQ', '["Proper node creation and pointer update", "Memory leak in node creation", "Incorrect pointer manipulation", "Missing null checks"]', 'Proper node creation and pointer update', 1);

-- Exercises for Graph Algorithms
INSERT INTO exercises (algorithm_id, title, description, difficulty, question_type, options, correct_answer, level_number) VALUES
(10, 'BFS Implementation', 'Implement Breadth-First Search using queue. Mark visited nodes properly.', 'medium', 'MCQ', '["Correct BFS with queue and visited set", "Missing visited node tracking", "Incorrect queue operations", "Wrong neighbor processing"]', 'Correct BFS with queue and visited set', 1),
(11, 'DFS Recursive Implementation', 'Implement Depth-First Search using recursion. Handle visited nodes.', 'medium', 'MCQ', '["Proper recursive DFS with visited tracking", "Missing base case", "Incorrect recursion logic", "No visited node check"]', 'Proper recursive DFS with visited tracking', 1),
(12, 'Dijkstra Shortest Path', 'Implement Dijkstra algorithm for shortest path in weighted graph.', 'hard', 'MCQ', '["Complete Dijkstra with priority queue", "Missing distance initialization", "Incorrect priority queue usage", "Wrong edge relaxation"]', 'Complete Dijkstra with priority queue', 1);

-- Exercises for Dynamic Programming
INSERT INTO exercises (algorithm_id, title, description, difficulty, question_type, options, correct_answer, level_number) VALUES
(13, '0/1 Knapsack DP Solution', 'Solve 0/1 Knapsack using dynamic programming approach with memoization.', 'hard', 'MCQ', '["Correct DP table with optimal substructure", "Missing memoization", "Incorrect state transition", "Wrong base case handling"]', 'Correct DP table with optimal substructure', 1),
(14, 'LCS Dynamic Programming', 'Find Longest Common Subsequence using DP table approach.', 'medium', 'MCQ', '["Proper LCS DP table construction", "Incorrect table initialization", "Wrong recurrence relation", "Missing boundary conditions"]', 'Proper LCS DP table construction', 1),
(15, 'Fibonacci with Memoization', 'Implement Fibonacci using dynamic programming to avoid redundant calculations.', 'easy', 'MCQ', '["Memoized Fibonacci with cache", "Missing memoization logic", "Incorrect base cases", "No optimization applied"]', 'Memoized Fibonacci with cache', 1);

-- Exercises for Greedy Algorithms
INSERT INTO exercises (algorithm_id, title, description, difficulty, question_type, options, correct_answer, level_number) VALUES
(16, 'Activity Selection Problem', 'Select maximum number of non-overlapping activities using greedy approach.', 'medium', 'MCQ', '["Correct greedy selection by end time", "Wrong sorting criteria", "Incorrect overlap check", "Missing activity validation"]', 'Correct greedy selection by end time', 1),
(17, 'Huffman Coding Tree', 'Build Huffman coding tree for given character frequencies.', 'hard', 'MCQ', '["Proper priority queue based tree construction", "Incorrect frequency handling", "Wrong tree building logic", "Missing character encoding"]', 'Proper priority queue based tree construction', 1),
(18, 'Fractional Knapsack Greedy', 'Solve fractional knapsack using greedy approach based on value-to-weight ratio.', 'medium', 'MCQ', '["Correct ratio-based greedy selection", "Wrong sorting criteria", "Incorrect fraction calculation", "Missing capacity check"]', 'Correct ratio-based greedy selection', 1);

-- Exercises for Divide & Conquer
INSERT INTO exercises (algorithm_id, title, description, difficulty, question_type, options, correct_answer, level_number) VALUES
(19, 'Recursive Binary Search', 'Implement binary search using divide and conquer recursion.', 'easy', 'MCQ', '["Proper recursive division with base case", "Missing base case", "Incorrect recursive calls", "Wrong mid calculation"]', 'Proper recursive division with base case', 1),
(20, 'Maximum Subarray Sum', 'Find maximum subarray sum using divide and conquer approach.', 'medium', 'MCQ', '["Correct divide and conquer with cross sum", "Missing cross subarray case", "Incorrect division logic", "Wrong sum calculation"]', 'Correct divide and conquer with cross sum', 1),
(21, 'Strassen Matrix Multiplication', 'Implement Strassen algorithm for efficient matrix multiplication.', 'hard', 'MCQ', '["Complete Strassen with 7 multiplications", "Incorrect submatrix division", "Wrong multiplication count", "Missing base case optimization"]', 'Complete Strassen with 7 multiplications', 1);

-- Exercises for Backtracking
INSERT INTO exercises (algorithm_id, title, description, difficulty, question_type, options, correct_answer, level_number) VALUES
(22, 'N-Queens Backtracking', 'Solve N-Queens problem using backtracking approach.', 'hard', 'MCQ', '["Complete backtracking with conflict checking", "Missing conflict detection", "Incorrect backtracking logic", "Wrong queen placement"]', 'Complete backtracking with conflict checking', 1),
(23, 'Sudoku Solver Implementation', 'Solve 9x9 Sudoku puzzle using backtracking algorithm.', 'hard', 'MCQ', '["Proper backtracking with validation", "Missing number validation", "Incorrect cell traversal", "Wrong backtracking condition"]', 'Proper backtracking with validation', 1),
(24, 'Subset Sum Backtracking', 'Find if subset exists with given sum using backtracking.', 'medium', 'MCQ', '["Correct inclusion/exclusion backtracking", "Missing sum validation", "Incorrect subset generation", "Wrong termination condition"]', 'Correct inclusion/exclusion backtracking', 1);
