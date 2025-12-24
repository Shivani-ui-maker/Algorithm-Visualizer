package com.algo.backend.service;

import com.algo.backend.entity.Badge;
import com.algo.backend.entity.User;
import com.algo.backend.entity.UserBadge;
import com.algo.backend.entity.UserProgress;
import com.algo.backend.repository.BadgeRepository;
import com.algo.backend.repository.UserBadgeRepository;
import com.algo.backend.repository.UserProgressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BadgeService {

    @Autowired
    private BadgeRepository badgeRepository;

    @Autowired
    private UserBadgeRepository userBadgeRepository;

    @Autowired
    private UserProgressRepository userProgressRepository;

    public void checkAndAwardBadges(User user) {
        List<Badge> availableBadges = badgeRepository.findByIsActiveTrue();
        
        for (Badge badge : availableBadges) {
            if (!userHasBadge(user, badge) && isEligibleForBadge(user, badge)) {
                awardBadge(user, badge);
            }
        }
    }

    private boolean userHasBadge(User user, Badge badge) {
        return userBadgeRepository.existsByUserAndBadge(user, badge);
    }

    private boolean isEligibleForBadge(User user, Badge badge) {
        switch (badge.getType()) {
            case ALGORITHMS_COMPLETED:
                long completedCount = userProgressRepository.countByUserAndStatus(user, UserProgress.Status.COMPLETED);
                return completedCount >= badge.getRequiredValue();
                
            case QUIZ_SCORE:
                // Check if user has achieved required quiz score
                return userProgressRepository.findByUser(user).stream()
                    .anyMatch(progress -> progress.getBestScore() != null && progress.getBestScore() >= badge.getRequiredValue());
                
            case FIRST_SUBMISSION:
                return userProgressRepository.countByUser(user) >= 1;
                
            case PERFECT_SCORE:
                return userProgressRepository.findByUser(user).stream()
                    .anyMatch(progress -> progress.getBestScore() != null && progress.getBestScore() == 100);
                
            case SPEED_DEMON:
                // Award if user completes algorithm in under required time (seconds)
                return userProgressRepository.findByUser(user).stream()
                    .anyMatch(progress -> progress.getTimeSpentSeconds() != null && 
                             progress.getTimeSpentSeconds() <= badge.getRequiredValue());
                
            default:
                return false;
        }
    }

    private void awardBadge(User user, Badge badge) {
        UserBadge userBadge = new UserBadge();
        userBadge.setUser(user);
        userBadge.setBadge(badge);
        userBadge.setEarnedAt(LocalDateTime.now());
        userBadgeRepository.save(userBadge);
    }

    public List<UserBadge> getUserBadges(User user) {
        return userBadgeRepository.findByUserOrderByEarnedAtDesc(user);
    }

    public List<Badge> getAllBadges() {
        return badgeRepository.findAll();
    }

    public void initializeDefaultBadges() {
        if (badgeRepository.count() == 0) {
            // Create default badges
            Badge firstSubmission = new Badge("First Steps", "Complete your first algorithm", Badge.BadgeType.FIRST_SUBMISSION, 1);
            Badge algorithmsCompleted5 = new Badge("Algorithm Explorer", "Complete 5 algorithms", Badge.BadgeType.ALGORITHMS_COMPLETED, 5);
            Badge algorithmsCompleted10 = new Badge("Algorithm Master", "Complete 10 algorithms", Badge.BadgeType.ALGORITHMS_COMPLETED, 10);
            Badge perfectScore = new Badge("Perfectionist", "Achieve a perfect quiz score", Badge.BadgeType.PERFECT_SCORE, 100);
            Badge speedDemon = new Badge("Speed Demon", "Complete an algorithm in under 30 seconds", Badge.BadgeType.SPEED_DEMON, 30);
            Badge quizMaster = new Badge("Quiz Master", "Score 90% or higher on a quiz", Badge.BadgeType.QUIZ_SCORE, 90);

            badgeRepository.save(firstSubmission);
            badgeRepository.save(algorithmsCompleted5);
            badgeRepository.save(algorithmsCompleted10);
            badgeRepository.save(perfectScore);
            badgeRepository.save(speedDemon);
            badgeRepository.save(quizMaster);
        }
    }
}
