package com.trainingcenter.controller;

import com.trainingcenter.dto.request.LoginRequest;
import com.trainingcenter.dto.response.ApiResponse;
import com.trainingcenter.dto.response.AuthResponse;
import com.trainingcenter.dto.response.UserResponse;
import com.trainingcenter.entity.Student;
import com.trainingcenter.entity.Trainer;
import com.trainingcenter.entity.UserAccount;
import com.trainingcenter.repository.StudentRepository;
import com.trainingcenter.repository.TrainerRepository;
import com.trainingcenter.repository.UserAccountRepository;
import com.trainingcenter.security.JwtTokenProvider;
import com.trainingcenter.security.UserPrincipal;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/auth")
@Slf4j
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private UserAccountRepository userAccountRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private TrainerRepository trainerRepository;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        log.info("Login request received for username: {}", loginRequest.getUsername());

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = tokenProvider.generateToken(authentication);
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        // Update last login timestamp
        Optional<UserAccount> userOpt = userAccountRepository.findById(userPrincipal.getId());
        if (userOpt.isPresent()) {
            UserAccount user = userOpt.get();
            user.setLastLogin(LocalDateTime.now());
            userAccountRepository.save(user);
        }

        List<String> roles = userPrincipal.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        AuthResponse authResponse = AuthResponse.builder()
                .token(jwt)
                .userId(userPrincipal.getId())
                .username(userPrincipal.getUsername())
                .email(userPrincipal.getEmail())
                .roles(roles)
                .build();

        log.info("User {} successfully authenticated", userPrincipal.getUsername());
        return ResponseEntity.ok(ApiResponse.success("Login successful", authResponse));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<String>> logoutUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            log.info("User {} logged out", auth.getName());
        }
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok(ApiResponse.success("Logout successful", "Token invalidated"));
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserResponse>> getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || auth.getPrincipal().equals("anonymousUser")) {
            return ResponseEntity.status(401).body(ApiResponse.error("Unauthorized"));
        }

        UserPrincipal userPrincipal = (UserPrincipal) auth.getPrincipal();
        UserAccount user = userAccountRepository.findById(userPrincipal.getId())
                .orElseThrow(() -> new RuntimeException("User profile not found"));

        List<String> roles = userPrincipal.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        UserResponse.UserResponseBuilder responseBuilder = UserResponse.builder()
                .userId(user.getUserId())
                .username(user.getUsername())
                .email(user.getEmail())
                .mobile(user.getMobile())
                .status(user.getStatus().name())
                .roles(roles);

        if (user.getCenter() != null) {
            responseBuilder.centerId(user.getCenter().getCenterId());
        }

        // Conditionally populate studentId or trainerId
        if (roles.contains("ROLE_STUDENT")) {
            studentRepository.findByUserUserId(user.getUserId())
                    .ifPresent(student -> {
                        responseBuilder.studentId(student.getStudentId());
                        responseBuilder.centerId(student.getCenter().getCenterId());
                    });
        } else if (roles.contains("ROLE_TRAINER")) {
            trainerRepository.findByUserUserId(user.getUserId())
                    .ifPresent(trainer -> {
                        responseBuilder.trainerId(trainer.getTrainerId());
                        responseBuilder.centerId(trainer.getCenter().getCenterId());
                    });
        }

        return ResponseEntity.ok(ApiResponse.success("User profile fetched", responseBuilder.build()));
    }
}
