package com.example.userserivce.service;

import com.example.userserivce.dto.request.LoginRequest;
import com.example.userserivce.dto.request.RefreshRequest;
import com.example.userserivce.dto.request.RegisterRequest;
import com.example.userserivce.dto.response.AuthResponse;
import com.example.userserivce.dto.response.UserResponse;

public interface AuthService {

    UserResponse register(RegisterRequest request);

    AuthResponse login(LoginRequest request);

    AuthResponse refresh(RefreshRequest request);
}
