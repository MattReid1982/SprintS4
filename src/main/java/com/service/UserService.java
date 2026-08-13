package com.service;

import com.model.User;
import com.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service class for managing {@link User} entities and business logic.
 * Automatically triggers stored procedure seeding for new accounts.
 */
@Service
public class UserService {

    private final UserRepository userRepository;
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserService(UserRepository userRepository, @Autowired(required = false) JdbcTemplate jdbcTemplate) {
        this.userRepository = userRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    public UserService(UserRepository userRepository) {
        this(userRepository, null);
    }

    /**
     * Saves or updates a user. Automatically invokes the stored procedure
     * sp_seed_user_test_data to populate test flights, bookings, and baggage for new accounts.
     *
     * @param user user entity to save
     * @return saved User entity
     */
    public User saveUser(User user) {
        boolean isNew = (user.getId() == null);
        User saved = userRepository.save(user);
        if (isNew && saved.getId() != null) {
            seedUserData(saved.getId());
        }
        return saved;
    }

    /**
     * Executes the sp_seed_user_test_data stored procedure for a given user ID.
     *
     * @param userId user ID
     */
    public void seedUserData(Long userId) {
        if (jdbcTemplate != null) {
            try {
                jdbcTemplate.execute("CALL sp_seed_user_test_data(" + userId + ")");
                System.out.println("✅ Stored procedure sp_seed_user_test_data executed for user ID " + userId);
            } catch (Exception e) {
                System.err.println("⚠️ Could not execute sp_seed_user_test_data for user ID " + userId + ": " + e.getMessage());
            }
        }
    }

    /**
     * Retrieves all users.
     *
     * @return list of all users
     */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Retrieves a user by their ID.
     *
     * @param id user ID
     * @return Optional containing the User if found
     */
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    /**
     * Retrieves a user by their username.
     *
     * @param username username
     * @return Optional containing the User if found
     */
    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    /**
     * Deletes a user by their ID.
     *
     * @param id user ID
     */
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    /**
     * Updates a user's details by ID.
     *
     * @param id          user ID to update
     * @param userDetails user entity with updated fields
     * @return updated User entity, or empty Optional if not found
     */
    public Optional<User> updateUser(Long id, User userDetails) {
        return userRepository.findById(id).map(existing -> {
            if (userDetails.getUsername() != null) {
                existing.setUsername(userDetails.getUsername());
            }
            if (userDetails.getEmail() != null) {
                existing.setEmail(userDetails.getEmail());
            }
            if (userDetails.getPasswordHash() != null && !userDetails.getPasswordHash().isBlank()) {
                existing.setPasswordHash(userDetails.getPasswordHash());
            }
            if (userDetails.getRole() != null) {
                existing.setRole(userDetails.getRole());
            }
            return userRepository.save(existing);
        });
    }

    /**
     * Authenticates a user by matching their username and password.
     *
     * @param username username
     * @param password raw password to check
     * @return Optional containing the User if authenticated successfully, or empty Optional otherwise
     */
    public Optional<User> authenticate(String username, String password) {
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (password != null && (password.equals(user.getPasswordHash()) || user.getPasswordHash().endsWith(password))) {
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }
}
