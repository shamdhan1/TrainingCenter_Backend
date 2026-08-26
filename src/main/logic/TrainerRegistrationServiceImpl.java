package com.trainingcenter.service.impl;

import com.trainingcenter.dto.request.TrainerRegistrationRequest;
import com.trainingcenter.dto.response.TrainerRegistrationResponse;
import com.trainingcenter.entity.Center;
import com.trainingcenter.entity.Role;
import com.trainingcenter.entity.Trainer;
import com.trainingcenter.entity.UserAccount;
import com.trainingcenter.enums.RoleName;
import com.trainingcenter.enums.UserStatus;
import com.trainingcenter.repository.CenterRepository;
import com.trainingcenter.repository.RoleRepository;
import com.trainingcenter.repository.TrainerRepository;
import com.trainingcenter.repository.UserAccountRepository;
import com.trainingcenter.service.TrainerRegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class TrainerRegistrationServiceImpl implements TrainerRegistrationService {

    private final TrainerRepository trainerRepository;
    private final UserAccountRepository userAccountRepository;
    private final RoleRepository roleRepository;
    private final CenterRepository centerRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public TrainerRegistrationResponse registerTrainer(TrainerRegistrationRequest request) {
        // 1. Verify username uniqueness
        String username = request.getUsername().trim().toLowerCase();
        if (userAccountRepository.existsByUsername(username)) {
            throw new RuntimeException("Username already exists: " + username);
        }

        // 2. Verify email uniqueness
        String email = request.getEmail().trim().toLowerCase();
        if (userAccountRepository.existsByEmail(email)) {
            throw new RuntimeException("Email already exists: " + email);
        }

        // 3. Verify employee code uniqueness
        String employeeCode = request.getEmployeeCode().trim().toUpperCase();
        if (trainerRepository.existsByEmployeeCode(employeeCode)) {
            throw new RuntimeException("Employee code already exists: " + employeeCode);
        }

        // 4. Fetch Center
        Center center = centerRepository.findById(request.getCenterId())
                .orElseThrow(() -> new RuntimeException("Center not found with ID: " + request.getCenterId()));

        // 5. Fetch Trainer Role
        Role role = roleRepository.findByRoleName(RoleName.ROLE_TRAINER)
                .orElseThrow(() -> new RuntimeException("Role ROLE_TRAINER not found"));

        // 6. Map UserAccount status
        UserStatus status = UserStatus.ACTIVE;
        if (request.getStatus() != null) {
            try {
                status = UserStatus.valueOf(request.getStatus().toUpperCase());
            } catch (IllegalArgumentException e) {
                // Keep default ACTIVE
            }
        }

        UserAccount user = UserAccount.builder()
                .username(username)
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .email(email)
                .mobile(request.getMobile())
                .status(status)
                .center(center)
                .roles(new HashSet<>(Set.of(role)))
                .build();

        UserAccount savedUser = userAccountRepository.save(user);

        // 7. Map and save Trainer profile (with extended fields)
        LocalDate joiningDate = request.getJoiningDate() != null ? request.getJoiningDate() : LocalDate.now();

        Trainer trainer = Trainer.builder()
                .user(savedUser)
                .center(center)
                .employeeCode(employeeCode)
                .name(request.getName())
                .mobile(request.getMobile())
                .email(email)
                .specialization(request.getSpecialization())
                .qualification(request.getQualification())
                .experienceYears(request.getExperienceYears())
                .joiningDate(joiningDate)
                .status(status)
                
                // Set Onboarding profile properties
                .gender(request.getGender())
                .dateOfBirth(request.getDateOfBirth())
                .aadhaarNo(request.getAadhaarNo())
                .panNo(request.getPanNo())
                .alternativeMobile(request.getAlternativeMobile())
                .address(request.getAddress())
                .city(request.getCity())
                .state(request.getState())
                .pincode(request.getPincode())
                .designation(request.getDesignation())
                .contractType(request.getContractType() != null ? request.getContractType().toUpperCase() : "FULL_TIME")
                .salary(request.getSalary())
                .bankName(request.getBankName())
                .bankAccountNumber(request.getBankAccountNumber())
                .ifscCode(request.getIfscCode())
                .bio(request.getBio())
                .build();

        Trainer savedTrainer = trainerRepository.save(trainer);

        // 8. Return response
        return TrainerRegistrationResponse.builder()
                .trainerId(savedTrainer.getTrainerId())
                .userId(savedUser.getUserId())
                .employeeCode(savedTrainer.getEmployeeCode())
                .name(savedTrainer.getName())
                .email(savedTrainer.getEmail())
                .centerName(center.getName())
                .status(status.name())
                .message("Trainer registered and onboarding profile saved successfully")
                
                // Map Onboarding output properties
                .gender(savedTrainer.getGender())
                .dateOfBirth(savedTrainer.getDateOfBirth())
                .aadhaarNo(savedTrainer.getAadhaarNo())
                .panNo(savedTrainer.getPanNo())
                .alternativeMobile(savedTrainer.getAlternativeMobile())
                .address(savedTrainer.getAddress())
                .city(savedTrainer.getCity())
                .state(savedTrainer.getState())
                .pincode(savedTrainer.getPincode())
                .designation(savedTrainer.getDesignation())
                .contractType(savedTrainer.getContractType())
                .salary(savedTrainer.getSalary())
                .bankName(savedTrainer.getBankName())
                .bankAccountNumber(savedTrainer.getBankAccountNumber())
                .ifscCode(savedTrainer.getIfscCode())
                .bio(savedTrainer.getBio())
                .build();
    }
}
