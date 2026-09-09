package com.trainingcenter.config;

import com.trainingcenter.entity.Role;
import com.trainingcenter.entity.UserAccount;
import com.trainingcenter.enums.RoleName;
import com.trainingcenter.enums.UserStatus;
import com.trainingcenter.repository.RoleRepository;
import com.trainingcenter.repository.UserAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashSet;

@Component
@Order(1)
public class DatabaseInitializer implements CommandLineRunner {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserAccountRepository userAccountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${app.admin.initial-username:admin}")
    private String adminUsername;

    @Value("${app.admin.initial-password:adminpassword}")
    private String adminPassword;

    @Value("${app.admin.initial-email:admin@trainingcenter.com}")
    private String adminEmail;

    @Override
    public void run(String... args) throws Exception {
        // 1. Seed Roles
        for (RoleName roleName : RoleName.values()) {
            if (roleRepository.findByRoleName(roleName).isEmpty()) {
                Role role = Role.builder()
                        .roleName(roleName)
                        .build();
                roleRepository.save(role);
            }
        }

        // 2. Seed Admin User
        if (!userAccountRepository.existsByUsername(adminUsername)) {
            Role adminRole = roleRepository.findByRoleName(RoleName.ROLE_ADMIN)
                    .orElseThrow(() -> new RuntimeException("ROLE_ADMIN not found"));

            UserAccount adminUser = UserAccount.builder()
                    .username(adminUsername)
                    .passwordHash(passwordEncoder.encode(adminPassword))
                    .email(adminEmail)
                    .status(UserStatus.ACTIVE)
                    .roles(new HashSet<>(Collections.singletonList(adminRole)))
                    .build();

            userAccountRepository.save(adminUser);
        }
    }
}
