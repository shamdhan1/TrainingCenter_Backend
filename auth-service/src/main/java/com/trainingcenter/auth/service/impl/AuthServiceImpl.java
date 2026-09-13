package com.trainingcenter.auth.service.impl;

import com.trainingcenter.auth.dto.request.LoginRequest;
import com.trainingcenter.auth.dto.request.UserCreateRequest;
import com.trainingcenter.auth.dto.response.AuthResponse;
import com.trainingcenter.auth.dto.response.UserResponse;
import com.trainingcenter.auth.entity.Role;
import com.trainingcenter.auth.entity.UserAccount;
import com.trainingcenter.auth.enums.RoleName;
import com.trainingcenter.auth.enums.UserStatus;
import com.trainingcenter.auth.repository.RoleRepository;
import com.trainingcenter.auth.repository.UserAccountRepository;
import com.trainingcenter.auth.security.JwtTokenProvider;
import com.trainingcenter.auth.security.UserPrincipal;
import com.trainingcenter.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final UserAccountRepository userAccountRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public AuthResponse login(LoginRequest request) {
        log.info("Authenticating user: {}", request.getUsername());

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = tokenProvider.generateToken(authentication);
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        // Update last login
        userAccountRepository.findById(userPrincipal.getId()).ifPresent(user -> {
            user.setLastLogin(LocalDateTime.now());
            userAccountRepository.save(user);
        });

        List<String> roles = userPrincipal.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return AuthResponse.builder()
                .token(token)
                .tokenType("Bearer")
                .userId(userPrincipal.getId())
                .username(userPrincipal.getUsername())
                .email(userPrincipal.getEmail())
                .roles(roles)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getPrincipal())) {
            throw new RuntimeException("Unauthorized: No authenticated user in context");
        }

        UserPrincipal userPrincipal = (UserPrincipal) auth.getPrincipal();
        UserAccount user = userAccountRepository.findById(userPrincipal.getId())
                .orElseThrow(() -> new RuntimeException("User profile not found for ID: " + userPrincipal.getId()));

        List<String> roles = user.getRoles().stream()
                .map(r -> r.getRoleName().name())
                .collect(Collectors.toList());

        return UserResponse.builder()
                .userId(user.getUserId())
                .username(user.getUsername())
                .email(user.getEmail())
                .mobile(user.getMobile())
                .status(user.getStatus().name())
                .centerId(user.getCenterId())
                .roles(roles)
                .build();
    }

    @Override
    @Transactional
    public UserResponse createInternalUser(UserCreateRequest request) {
        if (userAccountRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already exists: " + request.getUsername());
        }
        if (request.getEmail() != null && userAccountRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists: " + request.getEmail());
        }

        Set<Role> roles = new HashSet<>();
        if (request.getRoles() != null && !request.getRoles().isEmpty()) {
            for (String roleStr : request.getRoles()) {
                RoleName roleName = RoleName.valueOf(roleStr);
                Role role = roleRepository.findByRoleName(roleName)
                        .orElseGet(() -> roleRepository.save(Role.builder().roleName(roleName).build()));
                roles.add(role);
            }
        } else {
            Role defaultRole = roleRepository.findByRoleName(RoleName.ROLE_STUDENT)
                    .orElseGet(() -> roleRepository.save(Role.builder().roleName(RoleName.ROLE_STUDENT).build()));
            roles.add(defaultRole);
        }

        UserAccount user = UserAccount.builder()
                .username(request.getUsername())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .email(request.getEmail())
                .mobile(request.getMobile())
                .centerId(request.getCenterId())
                .status(UserStatus.ACTIVE)
                .roles(roles)
                .build();

        UserAccount saved = userAccountRepository.save(user);

        return UserResponse.builder()
                .userId(saved.getUserId())
                .username(saved.getUsername())
                .email(saved.getEmail())
                .mobile(saved.getMobile())
                .status(saved.getStatus().name())
                .centerId(saved.getCenterId())
                .roles(saved.getRoles().stream().map(r -> r.getRoleName().name()).collect(Collectors.toList()))
                .build();
    }

    @Override
    public boolean validateToken(String token) {
        return tokenProvider.validateToken(token);
    }
}
