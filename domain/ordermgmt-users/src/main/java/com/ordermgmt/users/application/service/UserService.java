package com.ordermgmt.users.application.service;

import com.ordermgmt.users.domain.exception.InvalidCredentialsException;
import com.ordermgmt.users.domain.exception.UserAlreadyExistsException;
import com.ordermgmt.users.domain.exception.UserNotFoundException;
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

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService implements RegisterUserUseCase, LoginUserUseCase {

    private final UserRepositoryPort userRepositoryPort;
    private final PasswordEncoder passwordEncoder;
    private final TokenGeneratorPort tokenGeneratorPort;

    @Override
    public AuthResult login(String username, String password) {
        User user = userRepositoryPort.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + username));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new InvalidCredentialsException("Invalid password for user: " + username);
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
            throw new UserAlreadyExistsException("Username already exists: " + user.getUsername());
        }
        if (userRepositoryPort.existsByEmail(user.getEmail())) {
            throw new UserAlreadyExistsException("Email already exists: " + user.getEmail());
        }

        User newUser = User.builder()
                .id(UUID.randomUUID().toString())
                .username(user.getUsername())
                .email(user.getEmail())
                .password(passwordEncoder.encode(user.getPassword()))
                .role(Role.ROLE_USER)
                .enabled(true)
                .build();

        return userRepositoryPort.save(newUser);
    }
}


