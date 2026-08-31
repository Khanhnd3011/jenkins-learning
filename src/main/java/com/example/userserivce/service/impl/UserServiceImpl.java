package com.example.userserivce.service.impl;

import com.example.userserivce.dto.request.AssignRolesRequest;
import com.example.userserivce.dto.request.UpdateUserRequest;
import com.example.userserivce.dto.response.UserResponse;
import com.example.userserivce.entity.Role;
import com.example.userserivce.entity.User;
import com.example.userserivce.exception.AppException;
import com.example.userserivce.exception.ErrorCode;
import com.example.userserivce.repository.RoleRepository;
import com.example.userserivce.repository.UserRepository;
import com.example.userserivce.security.UserPrincipal;
import com.example.userserivce.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserServiceImpl implements UserService {

    UserRepository userRepository;
    RoleRepository roleRepository;
    PasswordEncoder passwordEncoder;

    @Override
    public UserResponse getCurrentUser() {
        return toResponse(getAuthenticatedUser());
    }

    @Override
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public UserResponse getUser(String id) {
        return toResponse(findUser(id));
    }

    @Override
    @Transactional
    public UserResponse updateUser(String id, UpdateUserRequest request) {
        User current = getAuthenticatedUser();
        if (!current.getUserId().equals(id) && !hasAdminRole(current)) {
            throw new AccessDeniedException(ErrorCode.UNAUTHORIZED.getMessage());
        }

        User user = findUser(id);
        if (request.getEmail() != null && !request.getEmail().isBlank()) {
            if (!user.getEmail().equals(request.getEmail()) && userRepository.existsByEmail(request.getEmail())) {
                throw new AppException(ErrorCode.USER_EXISTED);
            }
            user.setEmail(request.getEmail());
        }
        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        return toResponse(userRepository.save(user));
    }

    @Override
    public void deleteUser(String id) {
        User user = findUser(id);
        userRepository.delete(user);
    }

    @Override
    @Transactional
    public UserResponse assignRoles(String id, AssignRolesRequest request) {
        User user = findUser(id);
        Set<Role> roles = new HashSet<>();
        for (String roleName : request.getRoles()) {
            Role role = roleRepository.findById(roleName)
                    .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));
            roles.add(role);
        }
        user.setRoles(roles);
        return toResponse(userRepository.save(user));
    }

    private User findUser(String id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
    }

    private User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof UserPrincipal principal)) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
        return findUser(principal.getUserId());
    }

    private boolean hasAdminRole(User user) {
        return user.getRoles().stream().anyMatch(role -> "ADMIN".equals(role.getName()));
    }

    private UserResponse toResponse(User user) {
        return UserResponse.builder()
                .userId(user.getUserId())
                .email(user.getEmail())
                .roles(user.getRoles().stream().map(Role::getName).collect(Collectors.toSet()))
                .build();
    }
}
