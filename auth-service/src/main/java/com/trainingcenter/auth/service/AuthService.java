package com.trainingcenter.auth.service;

import com.trainingcenter.auth.dto.request.LoginRequest;
import com.trainingcenter.auth.dto.request.UserCreateRequest;
import com.trainingcenter.auth.dto.response.AuthResponse;
import com.trainingcenter.auth.dto.response.UserResponse;

public interface AuthService {
    AuthResponse login(LoginRequest request);
    UserResponse getCurrentUser();
    UserResponse createInternalUser(UserCreateRequest request);
    boolean validateToken(String token);
}
