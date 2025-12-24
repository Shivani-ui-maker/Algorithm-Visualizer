package com.algo.backend.service;

import com.algo.backend.entity.User;
import com.algo.backend.repository.UserRepository;
import com.algo.backend.security.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Custom implementation of Spring Security's UserDetailsService interface.
 * Loads user-specific data for authentication and authorization.
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    
    private final UserRepository userRepository;

    @Autowired
    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Load user by username (email or username)
     * @param username the username identifying the user whose data is required.
     * @return UserDetails containing user information
     * @throws UsernameNotFoundException if user not found
     */
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        // Try to find by email (case-insensitive) or display name
        User user = userRepository.findByEmailIgnoreCase(usernameOrEmail)
            .or(() -> userRepository.findByDisplayName(usernameOrEmail))
            .orElseThrow(() -> new UsernameNotFoundException("User not found with username/email: " + usernameOrEmail));

        return UserDetailsImpl.build(user);
    }
    
    /**
     * Load user by email
     * @param email the email identifying the user
     * @return UserDetails containing user information
     * @throws UsernameNotFoundException if user not found
     */
    @Transactional(readOnly = true)
    public UserDetails loadUserByEmail(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
            
        return UserDetailsImpl.build(user);
    }
}
