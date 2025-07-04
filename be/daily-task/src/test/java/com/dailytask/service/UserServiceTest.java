package com.dailytask.service;

import com.dailytask.dto.request.CreateUserRequest;
import com.dailytask.dto.response.UserResponse;
import com.dailytask.entity.User;
import com.dailytask.exception.DuplicateResourceException;
import com.dailytask.exception.ResourceNotFoundException;
import com.dailytask.repository.UserRepository;
import com.dailytask.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");
        testUser.setCreatedAt(LocalDateTime.now());
        testUser.setUpdatedAt(LocalDateTime.now());
    }

    @Test
    void createUser_ShouldReturnUserResponse_WhenUsernameIsUnique() {
        CreateUserRequest request = new CreateUserRequest("newuser");
        when(userRepository.existsByUsername("newuser")).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        UserResponse response = userService.createUser(request);

        assertThat(response.username()).isEqualTo(testUser.getUsername());
        assertThat(response.id()).isEqualTo(testUser.getId());
        verify(userRepository).existsByUsername("newuser");
        verify(userRepository).save(any(User.class));
    }

    @Test
    void createUser_ShouldThrowDuplicateResourceException_WhenUsernameExists() {
        CreateUserRequest request = new CreateUserRequest("existinguser");
        when(userRepository.existsByUsername("existinguser")).thenReturn(true);

        assertThatThrownBy(() -> userService.createUser(request))
            .isInstanceOf(DuplicateResourceException.class)
            .hasMessageContaining("User already exists with username: 'existinguser'");

        verify(userRepository).existsByUsername("existinguser");
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void getUserById_ShouldReturnUserResponse_WhenUserExists() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        UserResponse response = userService.getUserById(1L);

        assertThat(response.id()).isEqualTo(testUser.getId());
        assertThat(response.username()).isEqualTo(testUser.getUsername());
        verify(userRepository).findById(1L);
    }

    @Test
    void getUserById_ShouldThrowResourceNotFoundException_WhenUserNotExists() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.getUserById(1L))
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessageContaining("User not found with id: '1'");

        verify(userRepository).findById(1L);
    }

    @Test
    void getUserByUsername_ShouldReturnUserResponse_WhenUserExists() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));

        UserResponse response = userService.getUserByUsername("testuser");

        assertThat(response.username()).isEqualTo(testUser.getUsername());
        verify(userRepository).findByUsername("testuser");
    }

    @Test
    void getAllUsers_ShouldReturnAllUsers() {
        List<User> users = List.of(testUser);
        when(userRepository.findAll()).thenReturn(users);

        List<UserResponse> responses = userService.getAllUsers();

        assertThat(responses).hasSize(1);
        assertThat(responses.get(0).username()).isEqualTo(testUser.getUsername());
        verify(userRepository).findAll();
    }

    @Test
    void searchUsers_ShouldReturnMatchingUsers() {
        List<User> users = List.of(testUser);
        when(userRepository.findByUsernameContainingIgnoreCase("test")).thenReturn(users);

        List<UserResponse> responses = userService.searchUsers("test");

        assertThat(responses).hasSize(1);
        assertThat(responses.get(0).username()).isEqualTo(testUser.getUsername());
        verify(userRepository).findByUsernameContainingIgnoreCase("test");
    }

    @Test
    void deleteUser_ShouldDeleteUser_WhenUserExists() {
        when(userRepository.existsById(1L)).thenReturn(true);

        userService.deleteUser(1L);

        verify(userRepository).existsById(1L);
        verify(userRepository).deleteById(1L);
    }

    @Test
    void deleteUser_ShouldThrowResourceNotFoundException_WhenUserNotExists() {
        when(userRepository.existsById(1L)).thenReturn(false);

        assertThatThrownBy(() -> userService.deleteUser(1L))
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessageContaining("User not found with id: '1'");

        verify(userRepository).existsById(1L);
        verify(userRepository, never()).deleteById(any());
    }

    @Test
    void existsByUsername_ShouldReturnTrue_WhenUserExists() {
        when(userRepository.existsByUsername("testuser")).thenReturn(true);

        boolean exists = userService.existsByUsername("testuser");

        assertThat(exists).isTrue();
        verify(userRepository).existsByUsername("testuser");
    }
} 