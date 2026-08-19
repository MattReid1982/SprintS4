package com.service;

import com.exception.ResourceNotFoundException;
import com.model.User;
import com.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service class for managing {@link User} entities and business logic.
 */
@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Saves or updates a user entity.
     *
     * @param user user entity to save
     * @return saved User entity
     */
    public User saveUser(User user) {
        return userRepository.save(user);
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
     * @throws ResourceNotFoundException if user with given ID is not found
     */
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User not found with id: " + id);
        }
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
            if (password != null && user.getPasswordHash() != null && password.equals(user.getPasswordHash())) {
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }
}
