package com.example.userserivce.service.impl;

import com.example.userserivce.dto.request.LoginRequest;
import com.example.userserivce.dto.request.RefreshRequest;
import com.example.userserivce.dto.request.RegisterRequest;
import com.example.userserivce.dto.response.AuthResponse;
import com.example.userserivce.dto.response.UserResponse;
import com.example.userserivce.entity.Role;
import com.example.userserivce.entity.User;
import com.example.userserivce.exception.AppException;
import com.example.userserivce.exception.ErrorCode;
import com.example.userserivce.repository.RoleRepository;
import com.example.userserivce.repository.UserRepository;
import com.example.userserivce.security.JwtService;
import com.example.userserivce.service.AuthService;
import com.nimbusds.jwt.SignedJWT;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthServiceImpl implements AuthService {

    UserRepository userRepository;
    RoleRepository roleRepository;
    PasswordEncoder passwordEncoder;
    JwtService jwtService;

    @Override
    @Transactional
    public UserResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }
        Role userRole = roleRepository.findById("USER")
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));

        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(new HashSet<>(Set.of(userRole)))
                .build();
        user = userRepository.save(user);
        return toResponse(user);
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new AppException(ErrorCode.INVALID_CREDENTIALS));
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new AppException(ErrorCode.INVALID_CREDENTIALS);
        }
        return issueTokens(user);
    }

    @Override
    public AuthResponse refresh(RefreshRequest request) {
        SignedJWT signedJWT = jwtService.verify(request.getRefreshToken(), JwtService.REFRESH);
        String userId;
        try {
            userId = signedJWT.getJWTClaimsSet().getSubject();
        } catch (ParseException e) {
            throw new AppException(ErrorCode.INVALID_TOKEN);
        }
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        return issueTokens(user);
    }

    private AuthResponse issueTokens(User user) {
        return AuthResponse.builder()
                .accessToken(jwtService.generateAccessToken(user))
                .accessExpiryTime(jwtService.accessExpiryTime())
                .refreshToken(jwtService.generateRefreshToken(user))
                .refreshExpiryTime(jwtService.refreshExpiryTime())
                .build();
    }

    private UserResponse toResponse(User user) {
        return UserResponse.builder()
                .userId(user.getUserId())
                .email(user.getEmail())
                .roles(user.getRoles().stream().map(Role::getName).collect(Collectors.toSet()))
                .build();
    }
}
