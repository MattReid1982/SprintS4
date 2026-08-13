package com.service;

import com.model.User;
import com.repo.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User testUser;

    @BeforeEach
    public void setUp() {
        testUser = new User("admin", "password123", "admin@airport.com");
        testUser.setId(1L);
    }

    @Test
    public void testSaveUser() {
        when(userRepository.save(testUser)).thenReturn(testUser);

        User saved = userService.saveUser(testUser);
        assertNotNull(saved);
        assertEquals("admin", saved.getUsername());
        verify(userRepository, times(1)).save(testUser);
    }

    @Test
    public void testGetAllUsers() {
        when(userRepository.findAll()).thenReturn(List.of(testUser));

        List<User> users = userService.getAllUsers();
        assertEquals(1, users.size());
        assertEquals("admin", users.get(0).getUsername());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    public void testGetUserById() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        Optional<User> found = userService.getUserById(1L);
        assertTrue(found.isPresent());
        assertEquals("admin", found.get().getUsername());
    }

    @Test
    public void testGetUserByUsername() {
        when(userRepository.findByUsername("admin")).thenReturn(Optional.of(testUser));

        Optional<User> found = userService.getUserByUsername("admin");
        assertTrue(found.isPresent());
        assertEquals("admin@airport.com", found.get().getEmail());
    }

    @Test
    public void testAuthenticateSuccess() {
        when(userRepository.findByUsername("admin")).thenReturn(Optional.of(testUser));

        Optional<User> authenticated = userService.authenticate("admin", "password123");
        assertTrue(authenticated.isPresent());
        assertEquals("admin", authenticated.get().getUsername());
    }

    @Test
    public void testAuthenticateFailure() {
        when(userRepository.findByUsername("admin")).thenReturn(Optional.of(testUser));

        Optional<User> authenticated = userService.authenticate("admin", "wrongpassword");
        assertFalse(authenticated.isPresent());
    }

    @Test
    public void testDeleteUserSuccess() {
        when(userRepository.existsById(1L)).thenReturn(true);
        doNothing().when(userRepository).deleteById(1L);

        userService.deleteUser(1L);
        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testDeleteUserNotFound() {
        when(userRepository.existsById(99L)).thenReturn(false);

        assertThrows(com.exception.ResourceNotFoundException.class, () -> userService.deleteUser(99L));
    }
}
