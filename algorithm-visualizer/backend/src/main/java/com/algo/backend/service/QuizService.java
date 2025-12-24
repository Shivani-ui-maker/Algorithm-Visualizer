package com.algo.backend.service;

import com.algo.backend.entity.Quiz;
import com.algo.backend.entity.QuizAttempt;
import com.algo.backend.entity.User;
import com.algo.backend.repository.QuizRepository;
import com.algo.backend.repository.QuizAttemptRepository;
import com.algo.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class QuizService {

    @Autowired
    private QuizRepository quizRepository;

    @Autowired
    private QuizAttemptRepository quizAttemptRepository;

    @Autowired
    private UserRepository userRepository;

    public List<Quiz> getQuizzesByAlgorithm(Long algorithmId) {
        return quizRepository.findByAlgorithmId(algorithmId);
    }

    public List<Quiz> getRandomQuizzesByAlgorithm(Long algorithmId, int limit) {
        return quizRepository.findRandomQuizzesByAlgorithm(algorithmId, limit);
    }

    public List<Quiz> getRandomQuizzes(int limit) {
        return quizRepository.findRandomQuizzes(limit);
    }

    public Optional<Quiz> getQuizById(Long id) {
        return quizRepository.findById(id);
    }

    public QuizAttempt submitQuizAnswer(Long userId, Long quizId, String selectedAnswer) {
        Optional<User> userOpt = userRepository.findById(userId);
        Optional<Quiz> quizOpt = quizRepository.findById(quizId);

        if (userOpt.isPresent() && quizOpt.isPresent()) {
            User user = userOpt.get();
            Quiz quiz = quizOpt.get();

            boolean isCorrect = quiz.getCorrectAnswer().equals(selectedAnswer);
            int scoreEarned = isCorrect ? 10 : 0; // 10 points for correct answer

            QuizAttempt attempt = new QuizAttempt(user, quiz, selectedAnswer, isCorrect);
            attempt.setScoreEarned(scoreEarned);

            // Update user's total score
            if (isCorrect) {
                user.setTotalScore(user.getTotalScore() + scoreEarned);
                userRepository.save(user);
            }

            return quizAttemptRepository.save(attempt);
        }
        return null;
    }

    public List<QuizAttempt> getUserQuizAttempts(Long userId) {
        return quizAttemptRepository.findByUserIdOrderByAttemptedAtDesc(userId);
    }

    public Long getUserCorrectAnswersCount(Long userId) {
        return quizAttemptRepository.countCorrectAttemptsByUser(userId);
    }

    public Double getUserAverageScore(Long userId) {
        return quizAttemptRepository.getAverageScoreByUser(userId);
    }

    public Quiz saveQuiz(Quiz quiz) {
        return quizRepository.save(quiz);
    }

    public void deleteQuiz(Long id) {
        quizRepository.deleteById(id);
    }
}
