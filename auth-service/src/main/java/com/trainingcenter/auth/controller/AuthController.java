package com.trainingcenter.auth.controller;

import com.trainingcenter.auth.dto.request.LoginRequest;
import com.trainingcenter.auth.dto.request.UserCreateRequest;
import com.trainingcenter.auth.dto.response.ApiResponse;
import com.trainingcenter.auth.dto.response.AuthResponse;
import com.trainingcenter.auth.dto.response.UserResponse;
import com.trainingcenter.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(ApiResponse.success("Login successful", response));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<String>> logout() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            log.info("User {} logged out", auth.getName());
        }
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok(ApiResponse.success("Logout successful", "Token invalidated"));
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserResponse>> getCurrentUser() {
        UserResponse response = authService.getCurrentUser();
        return ResponseEntity.ok(ApiResponse.success("User profile fetched successfully", response));
    }

    @PostMapping("/internal/users")
    public ResponseEntity<ApiResponse<UserResponse>> createInternalUser(@Valid @RequestBody UserCreateRequest request) {
        UserResponse response = authService.createInternalUser(request);
        return ResponseEntity.ok(ApiResponse.success("User account created successfully", response));
    }

    @GetMapping("/validate")
    public ResponseEntity<ApiResponse<Boolean>> validateToken(@RequestParam("token") String token) {
        boolean isValid = authService.validateToken(token);
        return ResponseEntity.ok(ApiResponse.success("Token validation result", isValid));
    }
}
