package com.algo.backend.controller;

import com.algo.backend.entity.Badge;
import com.algo.backend.entity.User;
import com.algo.backend.entity.UserBadge;
import com.algo.backend.repository.UserRepository;
import com.algo.backend.service.BadgeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/badges")
public class BadgeController {

    @Autowired
    private BadgeService badgeService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/user/{email}")
    public ResponseEntity<List<UserBadge>> getUserBadges(@PathVariable String email) {
        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        
        List<UserBadge> badges = badgeService.getUserBadges(user);
        return ResponseEntity.ok(badges);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Badge>> getAllBadges() {
        List<Badge> badges = badgeService.getAllBadges();
        return ResponseEntity.ok(badges);
    }

    @PostMapping("/check/{email}")
    public ResponseEntity<?> checkBadges(@PathVariable String email) {
        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        
        badgeService.checkAndAwardBadges(user);
        return ResponseEntity.ok(Map.of("message", "Badges checked and awarded"));
    }

    @PostMapping("/initialize")
    public ResponseEntity<?> initializeBadges() {
        badgeService.initializeDefaultBadges();
        return ResponseEntity.ok(Map.of("message", "Default badges initialized"));
    }
}
