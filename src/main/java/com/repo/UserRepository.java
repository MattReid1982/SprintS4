package com.repo;

import com.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data JPA Repository for the {@link User} entity.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Finds a user by their username.
     *
     * @param username username to search for
     * @return an Optional containing the User if found, or empty if not
     */
    Optional<User> findByUsername(String username);

    /**
     * Checks if a user exists with the given username.
     *
     * @param username username to check
     * @return true if a user exists, false otherwise
     */
    Boolean existsByUsername(String username);

    /**
     * Finds a user by their email.
     *
     * @param email email to search for
     * @return an Optional containing the User if found, or empty if not
     */
    Optional<User> findByEmail(String email);
}
