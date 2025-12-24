package com.algo.backend.repository;

import com.algo.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for User entity.
 * Provides methods to interact with the database for User-related operations.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    /**
     * Find a user by email (case-sensitive)
     */
    Optional<User> findByEmail(String email);
    
    /**
     * Find a user by display name (case-sensitive)
     */
    Optional<User> findByDisplayName(String displayName);
    
    /**
     * Check if an email exists (case-sensitive)
     */
    Boolean existsByEmail(String email);
    
    /**
     * Check if an email exists (case-insensitive)
     */
    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM User u WHERE LOWER(u.email) = LOWER(:email)")
    boolean existsByEmailIgnoreCase(@Param("email") String email);
    
    /**
     * Find a user by email (case-insensitive)
     */
    @Query("SELECT u FROM User u WHERE LOWER(u.email) = LOWER(:email)")
    Optional<User> findByEmailIgnoreCase(@Param("email") String email);
    
    /**
     * Find users by role
     */
    List<User> findByRole(User.Role role);
    
    /**
     * Count new users created today
     */
    @Query("SELECT COUNT(u) FROM User u WHERE u.createdAt >= CURRENT_DATE")
    Long countNewUsersToday();
    
    /**
     * Find all users ordered by creation date (newest first)
     */
    List<User> findAllByOrderByCreatedAtDesc();
    
    // Backward compatibility methods for existing controllers
    @Query("SELECT u FROM User u WHERE u.email = :username OR u.displayName = :username")
    Optional<User> findByUsername(@Param("username") String username);
    
    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM User u WHERE LOWER(u.email) = LOWER(:username) OR LOWER(u.displayName) = LOWER(:username)")
    boolean existsByUsernameIgnoreCase(@Param("username") String username);

    /**
     * Guest-only normalized display name uniqueness check.
     * Normalization: strip '.' and '_' and lowercase.
     * Guest detection: email ends with the provided suffix (e.g., "@temp.com").
     */
    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM User u " +
           "WHERE u.email LIKE CONCAT('%', :guestEmailSuffix) AND " +
           "LOWER(REPLACE(REPLACE(u.displayName, '.', ''), '_', '')) = LOWER(REPLACE(REPLACE(:name, '.', ''), '_', ''))")
    boolean guestDisplayNameExistsNormalized(@Param("name") String name, @Param("guestEmailSuffix") String guestEmailSuffix);
}
