package com.algo.backend.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
// import org.springframework.security.authentication.AuthenticationManager;
// import org.springframework.security.authentication.BadCredentialsException;
// import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
// import org.springframework.security.core.Authentication;
// import org.springframework.security.core.context.SecurityContextHolder;
// import org.springframework.security.core.userdetails.UsernameNotFoundException;
// import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.algo.backend.dto.JwtResponse;
import com.algo.backend.dto.LoginRequest;
import com.algo.backend.dto.MessageResponse;
import com.algo.backend.dto.SignupRequest;
import com.algo.backend.entity.User;
import com.algo.backend.repository.UserRepository;
// import com.algo.backend.security.UserDetailsImpl;
import com.algo.backend.util.JwtUtil;

@CrossOrigin(
    origins = {
        "http://localhost:4200",
        "http://localhost:4201",
        "http://127.0.0.1:4200",
        "http://127.0.0.1:4201"
    },
    maxAge = 3600
)
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    // @Autowired
    // private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    // @Autowired
    // private PasswordEncoder encoder;

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * Guest user onboarding with username only.
     * - Accepts a desired username (letters, numbers, underscores, dots)
     * - Ensures Instagram-like uniqueness among guests: case-insensitive and ignoring '.' and '_'
     */
    @PostMapping("/onboard")
    public ResponseEntity<?> onboardNewUser(@RequestBody @jakarta.validation.Valid com.algo.backend.dto.GuestOnboardRequest request) {
        try {
            final String GUEST_EMAIL_SUFFIX = "@temp.com";

            String desired = request.getUsername().trim();

            String candidate = desired;
            // Ensure uniqueness among guests with Instagram-like normalization
            int attempt = 0;
            while (userRepository.guestDisplayNameExistsNormalized(candidate, GUEST_EMAIL_SUFFIX)) {
                attempt++;
                candidate = desired + attempt; // append numeric suffix
                if (attempt > 50) { // safety guard
                    return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(new MessageResponse("Guest username is taken or too similar. Please try a different one."));
                }
            }

            String uuid = UUID.randomUUID().toString();

            User user = new User();
            user.setEmail("guest_" + uuid.substring(0, 8) + GUEST_EMAIL_SUFFIX);
            user.setDisplayName(candidate);
            user.setPasswordHash(UUID.randomUUID().toString()); // Temporarily without encoding
            user.setRole(User.Role.USER);

            userRepository.save(user);

            String jwt = jwtUtil.generateTokenFromEmail(user.getEmail());

            Map<String, String> response = new HashMap<>();
            response.put("uuid", user.getUuid());
            response.put("userId", user.getId().toString());
            response.put("username", user.getDisplayName());
            response.put("accessToken", jwt);
            response.put("tokenType", "Bearer");
            response.put("message", "User onboarded successfully.");

            return ResponseEntity.ok(response);

        } catch (DataIntegrityViolationException ex) {
            logger.error("Guest onboarding failed due to duplicate data", ex);
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new MessageResponse("Guest onboarding failed: duplicate user"));
        } catch (IllegalArgumentException ex) {
            logger.error("Invalid input during onboarding", ex);
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Invalid onboarding request: " + ex.getMessage()));
        }
    }

    /**
     * Login endpoint
     */
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody @jakarta.validation.Valid LoginRequest loginRequest) {
        try {
            // Simple authentication without security manager
            Optional<User> userOpt = userRepository.findByEmailIgnoreCase(loginRequest.getUsername().trim());
            
            if (userOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new MessageResponse("Authentication failed: Invalid email or password"));
            }
            
            User user = userOpt.get();
            
            // Simple password check (temporarily without encoding)
            if (!user.getPasswordHash().equals(loginRequest.getPassword())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new MessageResponse("Authentication failed: Invalid email or password"));
            }

            String jwt = jwtUtil.generateToken(user.getEmail());

            JwtResponse response = new JwtResponse(
                    jwt,
                    user.getId(),
                    user.getEmail(),
                    user.getUuid()
            );
            return ResponseEntity.ok(response);

        } catch (Exception ex) {
            logger.warn("Invalid login attempt for username: {}", loginRequest.getUsername(), ex);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new MessageResponse("Authentication failed: Invalid email or password"));
        }
    }

    /**
     * Signup endpoint
     */
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody SignupRequest signupRequest) {
        try {
            String email = signupRequest.getEmail() != null
                    ? signupRequest.getEmail().trim()
                    : signupRequest.getUsername().trim();

            if (userRepository.existsByEmailIgnoreCase(email)) {
                return ResponseEntity.badRequest()
                        .body(new MessageResponse("Error: Email is already taken!"));
            }

            User user = new User();
            user.setEmail(email);
            user.setDisplayName(signupRequest.getUsername());
            // Hash the password - temporarily disabled
            String hashedPassword = signupRequest.getPassword(); // encoder.encode(signupRequest.getPassword());
            user.setPasswordHash(hashedPassword);
            user.setRole(User.Role.USER);

            userRepository.save(user);

            // Authentication authentication = authenticationManager.authenticate(
            //         new UsernamePasswordAuthenticationToken(email, signupRequest.getPassword()));

            // SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtUtil.generateToken(user.getEmail());

            // UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

            JwtResponse response = new JwtResponse(
                    jwt,
                    user.getId(),
                    user.getEmail(),
                    user.getUuid()
            );
            return ResponseEntity.ok(response);

        } catch (DataIntegrityViolationException ex) {
            logger.error("Signup failed due to duplicate entry", ex);
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new MessageResponse("Email already exists!"));
        } catch (Exception ex) {
            logger.warn("Invalid signup credentials for email: {}", signupRequest.getEmail(), ex);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new MessageResponse("Authentication failed: Invalid credentials"));
        }
    }

    /**
     * Validate email endpoint
     */
    @GetMapping("/validate/{email}")
    public ResponseEntity<?> validateEmail(@PathVariable String email) {
        try {
            boolean exists = userRepository.findByEmailIgnoreCase(email).isPresent();
            Map<String, Object> response = new HashMap<>();
            response.put("exists", exists);
            response.put("email", email);

            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException ex) {
            logger.error("Invalid email format", ex);
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Invalid email format"));
        }
    }

    /**
     * Fetch user profile
     */
    @GetMapping("/profile/{email}")
    public ResponseEntity<?> getUserProfile(@PathVariable String email) {
        Optional<User> userOpt = userRepository.findByEmailIgnoreCase(email);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new MessageResponse("User not found with email: " + email));
        }

        User user = userOpt.get();
        Map<String, Object> profile = new HashMap<>();
        profile.put("id", user.getId());
        profile.put("email", user.getEmail());
        profile.put("displayName", user.getDisplayName());
        profile.put("role", user.getRole());
        profile.put("createdAt", user.getCreatedAt());
        profile.put("totalScore", user.getTotalScore());

        return ResponseEntity.ok(profile);
    }
}
