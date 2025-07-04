package com.dailytask.service;

import com.dailytask.dto.request.CreateUserRequest;
import com.dailytask.dto.response.UserResponse;

import java.util.List;

public interface UserService {
    UserResponse createUser(CreateUserRequest request);
    UserResponse getUserById(Long id);
    UserResponse getUserByUsername(String username);
    List<UserResponse> getAllUsers();
    List<UserResponse> searchUsers(String searchTerm);
    void deleteUser(Long id);
    boolean existsByUsername(String username);
} 