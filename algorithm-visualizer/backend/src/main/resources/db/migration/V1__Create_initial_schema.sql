-- Algorithm Visualizer Database Schema V1

-- Users table
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    uuid VARCHAR(36) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    display_name VARCHAR(100) NOT NULL,
    role VARCHAR(20) NOT NULL DEFAULT 'USER' CHECK (role IN ('USER', 'ADMIN')),
    total_score INT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_role ON users(role);

-- Algorithm categories table
CREATE TABLE algorithm_categories (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    slug VARCHAR(100) NOT NULL UNIQUE,
    description TEXT,
    order_index INT NOT NULL DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_categories_slug ON algorithm_categories(slug);
    INDEX idx_categories_order (order_index)
);

-- Algorithms table
CREATE TABLE algorithms (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    category_id BIGINT NOT NULL,
    name VARCHAR(100) NOT NULL,
    slug VARCHAR(100) NOT NULL UNIQUE,
    type ENUM('ARRAY', 'LINKED_LIST', 'STACK', 'QUEUE', 'TREE', 'GRAPH', 'DP') NOT NULL,
    difficulty ENUM('easy', 'medium', 'hard') NOT NULL DEFAULT 'medium',
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (category_id) REFERENCES alg_categories(id) ON DELETE CASCADE,
    INDEX idx_algorithms_slug (slug),
    INDEX idx_algorithms_category (category_id),
    INDEX idx_algorithms_type (type),
    INDEX idx_algorithms_difficulty (difficulty)
);

-- Algorithm content table
CREATE TABLE algorithm_content (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    algorithm_id BIGINT NOT NULL,
    time_complexity VARCHAR(100),
    space_complexity VARCHAR(100),
    real_life_example TEXT,
    pseudocode TEXT,
    code_py TEXT,
    code_java TEXT,
    code_cpp TEXT,
    visualization_steps JSON,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (algorithm_id) REFERENCES algorithms(id) ON DELETE CASCADE,
    INDEX idx_content_algorithm (algorithm_id)
);

-- Quizzes table
CREATE TABLE quizzes (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    algorithm_id BIGINT NOT NULL,
    title VARCHAR(255) NOT NULL,
    difficulty ENUM('easy', 'medium', 'hard') NOT NULL DEFAULT 'medium',
    num_questions INT NOT NULL DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (algorithm_id) REFERENCES algorithms(id) ON DELETE CASCADE,
    INDEX idx_quizzes_algorithm (algorithm_id),
    INDEX idx_quizzes_difficulty (difficulty)
);

-- Quiz questions table
CREATE TABLE quiz_questions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    quiz_id BIGINT NOT NULL,
    question_text TEXT NOT NULL,
    options_json JSON NOT NULL,
    correct_option_index INT NOT NULL,
    explanation_text TEXT,
    order_index INT NOT NULL DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (quiz_id) REFERENCES quizzes(id) ON DELETE CASCADE,
    INDEX idx_questions_quiz (quiz_id),
    INDEX idx_questions_order (order_index)
);

-- Quiz submissions table
CREATE TABLE quiz_submissions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    quiz_id BIGINT NOT NULL,
    user_id BIGINT NULL,
    guest_session_id VARCHAR(255) NULL,
    score INT NOT NULL DEFAULT 0,
    total INT NOT NULL DEFAULT 0,
    answers_json JSON,
    started_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    submitted_at TIMESTAMP NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (quiz_id) REFERENCES quizzes(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE SET NULL,
    INDEX idx_submissions_quiz (quiz_id),
    INDEX idx_submissions_user (user_id),
    INDEX idx_submissions_guest (guest_session_id),
    INDEX idx_submissions_score (score)
);

-- User progress table
CREATE TABLE user_progress (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    algorithm_id BIGINT NOT NULL,
    status ENUM('VIEWED', 'COMPLETED') NOT NULL DEFAULT 'VIEWED',
    best_score INT DEFAULT 0,
    attempts INT DEFAULT 0,
    time_spent_seconds INT DEFAULT 0,
    last_viewed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    completed_at TIMESTAMP NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (algorithm_id) REFERENCES algorithms(id) ON DELETE CASCADE,
    UNIQUE KEY uk_user_algorithm (user_id, algorithm_id),
    INDEX idx_progress_user (user_id),
    INDEX idx_progress_algorithm (algorithm_id),
    INDEX idx_progress_status (status)
);

-- Badges table
CREATE TABLE badges (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    threshold INT NOT NULL,
    tier ENUM('PASS', 'SILVER', 'GOLD') NOT NULL,
    icon_url VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_badges_tier (tier),
    INDEX idx_badges_threshold (threshold)
);

-- User badges table
CREATE TABLE user_badges (
    user_id BIGINT NOT NULL,
    badge_id BIGINT NOT NULL,
    awarded_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (user_id, badge_id),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (badge_id) REFERENCES badges(id) ON DELETE CASCADE,
    INDEX idx_user_badges_user (user_id),
    INDEX idx_user_badges_badge (badge_id)
);

-- Leaderboard snapshots table
CREATE TABLE leaderboard_snapshots (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    period ENUM('ALL_TIME', 'MONTH', 'WEEK') NOT NULL,
    user_id BIGINT NOT NULL,
    total_score INT NOT NULL DEFAULT 0,
    algorithms_completed INT NOT NULL DEFAULT 0,
    badges_earned INT NOT NULL DEFAULT 0,
    rank_position INT NOT NULL,
    generated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_leaderboard_period (period),
    INDEX idx_leaderboard_rank (rank_position),
    INDEX idx_leaderboard_score (total_score DESC)
);

-- Exercises table (for coding challenges)
CREATE TABLE exercises (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    algorithm_id BIGINT NOT NULL,
    title VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    difficulty ENUM('easy', 'medium', 'hard') NOT NULL DEFAULT 'medium',
    level_number INT NOT NULL,
    starter_code_py TEXT,
    starter_code_java TEXT,
    starter_code_cpp TEXT,
    test_cases JSON,
    expected_output JSON,
    hints JSON,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (algorithm_id) REFERENCES algorithms(id) ON DELETE CASCADE,
    INDEX idx_exercises_algorithm (algorithm_id),
    INDEX idx_exercises_level (level_number),
    INDEX idx_exercises_difficulty (difficulty)
);

-- Exercise submissions table
CREATE TABLE exercise_submissions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    exercise_id BIGINT NOT NULL,
    user_id BIGINT NULL,
    guest_session_id VARCHAR(255) NULL,
    language ENUM('python', 'java', 'cpp') NOT NULL,
    code TEXT NOT NULL,
    status ENUM('PENDING', 'RUNNING', 'PASSED', 'FAILED', 'ERROR') NOT NULL DEFAULT 'PENDING',
    test_results JSON,
    execution_time_ms INT,
    memory_usage_kb INT,
    submitted_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (exercise_id) REFERENCES exercises(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE SET NULL,
    INDEX idx_exercise_submissions_exercise (exercise_id),
    INDEX idx_exercise_submissions_user (user_id),
    INDEX idx_exercise_submissions_status (status)
);
