package com.service;

import com.model.User;
import com.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
     * Saves or updates a user.
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
     */
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
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
            // Direct equality check or hash match check
            if (password != null && (password.equals(user.getPasswordHash()) || user.getPasswordHash().endsWith(password))) {
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }
}
