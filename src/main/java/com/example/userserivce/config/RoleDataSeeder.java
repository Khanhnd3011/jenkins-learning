package com.example.userserivce.config;

import com.example.userserivce.entity.Role;
import com.example.userserivce.entity.User;
import com.example.userserivce.repository.RoleRepository;
import com.example.userserivce.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleDataSeeder implements ApplicationRunner {

    RoleRepository roleRepository;
    UserRepository userRepository;
    PasswordEncoder passwordEncoder;

    @Override
    public void run(ApplicationArguments args) {
        Role userRole = seedRole("USER", "Default user role");
        Role adminRole = seedRole("ADMIN", "Administrator role");
        seedAdmin(adminRole, userRole);
    }

    private Role seedRole(String name, String description) {
        return roleRepository.findById(name).orElseGet(() -> roleRepository.save(Role.builder()
                .name(name)
                .description(description)
                .build()));
    }

    private void seedAdmin(Role adminRole, Role userRole) {
        if (userRepository.existsByEmail("admin@localhost")) {
            return;
        }
        userRepository.save(User.builder()
                .email("admin@localhost")
                .password(passwordEncoder.encode("admin123"))
                .roles(new HashSet<>(Set.of(adminRole, userRole)))
                .build());
    }
}
