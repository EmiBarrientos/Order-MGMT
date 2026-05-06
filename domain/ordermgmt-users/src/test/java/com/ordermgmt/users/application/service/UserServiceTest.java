package com.ordermgmt.users.application.service;

import com.ordermgmt.users.domain.exception.InvalidCredentialsException;
import com.ordermgmt.users.domain.exception.UserAlreadyExistsException;
import com.ordermgmt.users.domain.exception.UserNotFoundException;
import com.ordermgmt.users.domain.model.AuthResult;
import com.ordermgmt.users.domain.model.Role;
import com.ordermgmt.users.domain.model.User;
import com.ordermgmt.users.domain.port.out.TokenGeneratorPort;
import com.ordermgmt.users.domain.port.out.UserRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepositoryPort userRepositoryPort;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private TokenGeneratorPort tokenGeneratorPort;

    @InjectMocks
    private UserService userService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .id("123e4567-e89b-12d3-a456-426614174000")
                .username("testuser")
                .email("test@mail.com")
                .password("hashedPassword")
                .role(Role.ROLE_USER)
                .enabled(true)
                .build();
    }

    // ========== REGISTER TESTS ==========

    @Test
    @DisplayName("Should register user successfully")
    void shouldRegisterUserSuccessfully() {
        // Arrange
        User input = User.builder()
                .username("testuser")
                .email("test@mail.com")
                .password("123456")
                .build();

        when(userRepositoryPort.existsByUsername("testuser")).thenReturn(false);
        when(userRepositoryPort.existsByEmail("test@mail.com")).thenReturn(false);
        when(passwordEncoder.encode("123456")).thenReturn("hashedPassword");
        when(userRepositoryPort.save(any(User.class))).thenReturn(testUser);

        // Act
        User result = userService.register(input);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getUsername()).isEqualTo("testuser");
        assertThat(result.getRole()).isEqualTo(Role.ROLE_USER);
        assertThat(result.isEnabled()).isTrue();
        verify(userRepositoryPort).save(any(User.class));
    }

    @Test
    @DisplayName("Should throw UserAlreadyExistsException when username exists")
    void shouldThrowExceptionWhenUsernameExists() {
        // Arrange
        User input = User.builder()
                .username("testuser")
                .email("test@mail.com")
                .password("123456")
                .build();

        when(userRepositoryPort.existsByUsername("testuser")).thenReturn(true);

        // Act & Assert
        assertThatThrownBy(() -> userService.register(input))
                .isInstanceOf(UserAlreadyExistsException.class)
                .hasMessageContaining("testuser");

        verify(userRepositoryPort, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Should throw UserAlreadyExistsException when email exists")
    void shouldThrowExceptionWhenEmailExists() {
        // Arrange
        User input = User.builder()
                .username("testuser")
                .email("test@mail.com")
                .password("123456")
                .build();

        when(userRepositoryPort.existsByUsername("testuser")).thenReturn(false);
        when(userRepositoryPort.existsByEmail("test@mail.com")).thenReturn(true);

        // Act & Assert
        assertThatThrownBy(() -> userService.register(input))
                .isInstanceOf(UserAlreadyExistsException.class)
                .hasMessageContaining("test@mail.com");

        verify(userRepositoryPort, never()).save(any(User.class));
    }

    // ========== LOGIN TESTS ==========

    @Test
    @DisplayName("Should login successfully and return token")
    void shouldLoginSuccessfully() {
        // Arrange
        when(userRepositoryPort.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches("123456", "hashedPassword")).thenReturn(true);
        when(tokenGeneratorPort.generateToken(testUser)).thenReturn("jwt.token.here");

        // Act
        AuthResult result = userService.login("testuser", "123456");

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getToken()).isEqualTo("jwt.token.here");
        assertThat(result.getUser().getUsername()).isEqualTo("testuser");
    }

    @Test
    @DisplayName("Should throw UserNotFoundException when user does not exist")
    void shouldThrowExceptionWhenUserNotFound() {
        // Arrange
        when(userRepositoryPort.findByUsername("noexiste")).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> userService.login("noexiste", "123456"))
                .isInstanceOf(UserNotFoundException.class);

        verify(tokenGeneratorPort, never()).generateToken(any(User.class));
    }

    @Test
    @DisplayName("Should throw InvalidCredentialsException when password is wrong")
    void shouldThrowExceptionWhenPasswordIsWrong() {
        // Arrange
        when(userRepositoryPort.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches("wrongpassword", "hashedPassword")).thenReturn(false);

        // Act & Assert
        assertThatThrownBy(() -> userService.login("testuser", "wrongpassword"))
                .isInstanceOf(InvalidCredentialsException.class);

        verify(tokenGeneratorPort, never()).generateToken(any(User.class));
    }
}