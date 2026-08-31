package com.example.userserivce.service;

import com.example.userserivce.dto.request.AssignRolesRequest;
import com.example.userserivce.dto.request.UpdateUserRequest;
import com.example.userserivce.dto.response.UserResponse;

import java.util.List;

public interface UserService {

    UserResponse getCurrentUser();

    List<UserResponse> getAllUsers();

    UserResponse getUser(String id);

    UserResponse updateUser(String id, UpdateUserRequest request);

    void deleteUser(String id);

    UserResponse assignRoles(String id, AssignRolesRequest request);
}
