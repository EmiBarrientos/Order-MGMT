package com.ordermgmt.users.infrastructure.adapter.in.web;


import com.ordermgmt.users.domain.model.AuthResult;
import com.ordermgmt.users.domain.model.User;
import com.ordermgmt.users.domain.port.in.LoginUserUseCase;
import com.ordermgmt.users.domain.port.in.RegisterUserUseCase;
import com.ordermgmt.users.infrastructure.adapter.in.web.dto.AuthResponse;
import com.ordermgmt.users.infrastructure.adapter.in.web.dto.LoginRequest;
import com.ordermgmt.users.infrastructure.adapter.in.web.dto.RegisterRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final RegisterUserUseCase registerUserUseCase;
    private final LoginUserUseCase loginUserUseCase;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(request.getPassword())
                .build();

        User registered = registerUserUseCase.register(user);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                AuthResponse.builder()
                        .username(registered.getUsername())
                        .role(registered.getRole().name())
                        .build()
        );
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        AuthResult result = loginUserUseCase.login(request.getUsername(), request.getPassword());

        return ResponseEntity.ok(
                AuthResponse.builder()
                        .token(result.getToken())
                        .username(result.getUser().getUsername())
                        .role(result.getUser().getRole().name())
                        .build()
        );
    }
}