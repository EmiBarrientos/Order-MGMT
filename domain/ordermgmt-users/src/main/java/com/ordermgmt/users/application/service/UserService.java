package com.ordermgmt.users.application.service;

import com.ordermgmt.users.domain.model.AuthResult;
import com.ordermgmt.users.domain.model.Role;
import com.ordermgmt.users.domain.model.User;
import com.ordermgmt.users.domain.port.in.LoginUserUseCase;
import com.ordermgmt.users.domain.port.in.RegisterUserUseCase;
import com.ordermgmt.users.domain.port.out.TokenGeneratorPort;
import com.ordermgmt.users.domain.port.out.UserRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService implements RegisterUserUseCase, LoginUserUseCase {

    private final UserRepositoryPort userRepositoryPort;
    private final PasswordEncoder passwordEncoder;
    private final TokenGeneratorPort tokenGeneratorPort;

    @Override
    public AuthResult login(String username, String password) {
        User user = userRepositoryPort.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        String token = tokenGeneratorPort.generateToken(user);
        return AuthResult.builder()
                .token(token)
                .user(user)
                .build();

    }

    @Override
    public User register(User user) {
        if (userRepositoryPort.existsByUsername(user.getUsername())) {
            throw new RuntimeException("Username already exists");
        }
        if (userRepositoryPort.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        User newUser = User.builder()
                .username(user.getUsername())
                .email(user.getEmail())
                .password(passwordEncoder.encode(user.getPassword()))
                .role(Role.ROLE_USER)
                .enabled(true)
                .build();

        return userRepositoryPort.save(newUser);
    }
}


