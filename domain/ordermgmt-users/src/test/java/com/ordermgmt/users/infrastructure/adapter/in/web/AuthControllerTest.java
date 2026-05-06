package com.ordermgmt.users.infrastructure.adapter.in.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ordermgmt.users.domain.exception.InvalidCredentialsException;
import com.ordermgmt.users.domain.exception.UserAlreadyExistsException;
import com.ordermgmt.users.domain.exception.UserNotFoundException;
import com.ordermgmt.users.domain.model.AuthResult;
import com.ordermgmt.users.domain.model.Role;
import com.ordermgmt.users.domain.model.User;
import com.ordermgmt.users.domain.port.in.LoginUserUseCase;
import com.ordermgmt.users.domain.port.in.RegisterUserUseCase;
import com.ordermgmt.users.infrastructure.adapter.in.web.dto.LoginRequest;
import com.ordermgmt.users.infrastructure.adapter.in.web.dto.RegisterRequest;
import com.ordermgmt.users.infrastructure.config.SecurityConfig;
import com.ordermgmt.users.infrastructure.security.JwtFilter;
import com.ordermgmt.users.infrastructure.security.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
@Import({SecurityConfig.class, JwtFilter.class})
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private RegisterUserUseCase registerUserUseCase;

    @MockBean
    private LoginUserUseCase loginUserUseCase;

    @MockBean
    private JwtService jwtService;

    private User testUser;
    private AuthResult testAuthResult;

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

        testAuthResult = AuthResult.builder()
                .token("jwt.token.here")
                .user(testUser)
                .build();
    }

    // ========== REGISTER TESTS ==========

    @Test
    @DisplayName("Should register user and return 201")
    void shouldRegisterUserSuccessfully() throws Exception {
        // Arrange
        RegisterRequest request = new RegisterRequest("testuser", "test@mail.com", "123456");
        when(registerUserUseCase.register(any(User.class))).thenReturn(testUser);

        // Act & Assert
        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value("testuser"))
                .andExpect(jsonPath("$.role").value("ROLE_USER"));
    }

    @Test
    @DisplayName("Should return 409 when username already exists")
    void shouldReturn409WhenUsernameExists() throws Exception {
        // Arrange
        RegisterRequest request = new RegisterRequest("testuser", "test@mail.com", "123456");
        when(registerUserUseCase.register(any(User.class)))
                .thenThrow(new UserAlreadyExistsException("Username already exists: testuser"));

        // Act & Assert
        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409));
    }

    @Test
    @DisplayName("Should return 400 when register request is invalid")
    void shouldReturn400WhenRegisterRequestIsInvalid() throws Exception {
        // Arrange
        RegisterRequest request = new RegisterRequest("ab", "emailinvalido", "123");

        // Act & Assert
        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").exists());
    }

    // ========== LOGIN TESTS ==========

    @Test
    @DisplayName("Should login and return token")
    void shouldLoginSuccessfully() throws Exception {
        // Arrange
        LoginRequest request = new LoginRequest("testuser", "123456");
        when(loginUserUseCase.login(anyString(), anyString())).thenReturn(testAuthResult);

        // Act & Assert
        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("jwt.token.here"))
                .andExpect(jsonPath("$.username").value("testuser"))
                .andExpect(jsonPath("$.role").value("ROLE_USER"));
    }

    @Test
    @DisplayName("Should return 401 when user not found")
    void shouldReturn401WhenUserNotFound() throws Exception {
        // Arrange
        LoginRequest request = new LoginRequest("noexiste", "123456");
        when(loginUserUseCase.login(anyString(), anyString()))
                .thenThrow(new UserNotFoundException("User not found: noexiste"));

        // Act & Assert
        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value(401));
    }

    @Test
    @DisplayName("Should return 401 when password is wrong")
    void shouldReturn401WhenPasswordIsWrong() throws Exception {
        // Arrange
        LoginRequest request = new LoginRequest("testuser", "wrongpassword");
        when(loginUserUseCase.login(anyString(), anyString()))
                .thenThrow(new InvalidCredentialsException("Invalid password for user: testuser"));

        // Act & Assert
        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value(401));
    }
}