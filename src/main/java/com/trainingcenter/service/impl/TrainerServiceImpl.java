package com.trainingcenter.service.impl;

import com.trainingcenter.dto.request.TrainerRequest;
import com.trainingcenter.dto.response.TrainerResponse;
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
import com.trainingcenter.service.TrainerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.HashSet;

@Service
public class TrainerServiceImpl implements TrainerService {

    @Autowired
    private TrainerRepository trainerRepository;

    @Autowired
    private UserAccountRepository userAccountRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private CenterRepository centerRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional(readOnly = true)
    public Page<TrainerResponse> searchTrainers(String query, String status, Long centerId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        UserStatus userStatus = null;
        if (status != null && !status.trim().isEmpty()) {
            userStatus = UserStatus.valueOf(status.toUpperCase());
        }
        return trainerRepository.searchTrainers(query, userStatus, centerId, pageable)
                .map(this::mapToResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public TrainerResponse getTrainerById(Long id) {
        Trainer trainer = trainerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Trainer not found with ID: " + id));
        return mapToResponse(trainer);
    }

    @Override
    @Transactional(readOnly = true)
    public TrainerResponse getTrainerByUserId(Long userId) {
        Trainer trainer = trainerRepository.findByUserUserId(userId)
                .orElseThrow(() -> new RuntimeException("Trainer not found with User ID: " + userId));
        return mapToResponse(trainer);
    }

    @Override
    @Transactional
    public TrainerResponse createTrainer(TrainerRequest request) {
        logValidation(request.getUsername(), request.getEmail(), request.getEmployeeCode(), null);

        Center center = centerRepository.findById(request.getCenterId())
                .orElseThrow(() -> new RuntimeException("Center not found with ID: " + request.getCenterId()));

        Role role = roleRepository.findByRoleName(RoleName.ROLE_TRAINER)
                .orElseGet(() -> roleRepository.save(Role.builder().roleName(RoleName.ROLE_TRAINER).build()));

        UserStatus userStatus = UserStatus.valueOf(request.getStatus().toUpperCase());

        // 1. Create UserAccount
        UserAccount user = UserAccount.builder()
                .username(request.getUsername().toLowerCase().trim())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .email(request.getEmail().toLowerCase().trim())
                .mobile(request.getMobile())
                .status(userStatus)
                .center(center)
                .roles(new HashSet<>(Collections.singletonList(role)))
                .build();

        UserAccount savedUser = userAccountRepository.save(user);

        // 2. Create Trainer profile
        Trainer trainer = Trainer.builder()
                .user(savedUser)
                .center(center)
                .employeeCode(request.getEmployeeCode().toUpperCase().trim())
                .name(request.getName())
                .mobile(request.getMobile())
                .email(request.getEmail().toLowerCase().trim())
                .specialization(request.getSpecialization())
                .qualification(request.getQualification())
                .experienceYears(request.getExperienceYears())
                .joiningDate(request.getJoiningDate() != null ? request.getJoiningDate() : java.time.LocalDate.now())
                .status(userStatus)
                .build();

        Trainer savedTrainer = trainerRepository.save(trainer);
        return mapToResponse(savedTrainer);
    }

    @Override
    @Transactional
    public TrainerResponse updateTrainer(Long id, TrainerRequest request) {
        Trainer trainer = trainerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Trainer not found with ID: " + id));

        logValidation(request.getUsername(), request.getEmail(), request.getEmployeeCode(), trainer);

        Center center = centerRepository.findById(request.getCenterId())
                .orElseThrow(() -> new RuntimeException("Center not found with ID: " + request.getCenterId()));

        UserStatus userStatus = UserStatus.valueOf(request.getStatus().toUpperCase());

        // Update UserAccount
        UserAccount user = trainer.getUser();
        user.setUsername(request.getUsername().toLowerCase().trim());
        if (request.getPassword() != null && !request.getPassword().trim().isEmpty() && !request.getPassword().equals("********")) {
            user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        }
        user.setEmail(request.getEmail().toLowerCase().trim());
        user.setMobile(request.getMobile());
        user.setStatus(userStatus);
        user.setCenter(center);
        userAccountRepository.save(user);

        // Update Trainer profile
        trainer.setCenter(center);
        trainer.setEmployeeCode(request.getEmployeeCode().toUpperCase().trim());
        trainer.setName(request.getName());
        trainer.setMobile(request.getMobile());
        trainer.setEmail(request.getEmail().toLowerCase().trim());
        trainer.setSpecialization(request.getSpecialization());
        trainer.setQualification(request.getQualification());
        trainer.setExperienceYears(request.getExperienceYears());
        trainer.setStatus(userStatus);
        if (request.getJoiningDate() != null) {
            trainer.setJoiningDate(request.getJoiningDate());
        }

        Trainer updatedTrainer = trainerRepository.save(trainer);
        return mapToResponse(updatedTrainer);
    }

    @Override
    @Transactional
    public void deleteTrainer(Long id) {
        Trainer trainer = trainerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Trainer not found with ID: " + id));
        // Delete Trainer profile and UserAccount
        trainerRepository.delete(trainer);
    }

    private void logValidation(String username, String email, String empCode, Trainer existingTrainer) {
        if (existingTrainer == null) {
            if (userAccountRepository.existsByUsername(username.toLowerCase().trim())) {
                throw new RuntimeException("Username already exists: " + username);
            }
            if (userAccountRepository.existsByEmail(email.toLowerCase().trim())) {
                throw new RuntimeException("Email already exists: " + email);
            }
            if (trainerRepository.existsByEmployeeCode(empCode.toUpperCase().trim())) {
                throw new RuntimeException("Employee code already exists: " + empCode);
            }
        } else {
            UserAccount existingUser = existingTrainer.getUser();
            if (!existingUser.getUsername().equalsIgnoreCase(username.trim()) &&
                    userAccountRepository.existsByUsername(username.toLowerCase().trim())) {
                throw new RuntimeException("Username already exists: " + username);
            }
            if (!existingUser.getEmail().equalsIgnoreCase(email.trim()) &&
                    userAccountRepository.existsByEmail(email.toLowerCase().trim())) {
                throw new RuntimeException("Email already exists: " + email);
            }
            if (!existingTrainer.getEmployeeCode().equalsIgnoreCase(empCode.trim()) &&
                    trainerRepository.existsByEmployeeCode(empCode.toUpperCase().trim())) {
                throw new RuntimeException("Employee code already exists: " + empCode);
            }
        }
    }

    private TrainerResponse mapToResponse(Trainer trainer) {
        return TrainerResponse.builder()
                .trainerId(trainer.getTrainerId())
                .userId(trainer.getUser().getUserId())
                .username(trainer.getUser().getUsername())
                .centerId(trainer.getCenter().getCenterId())
                .centerName(trainer.getCenter().getName())
                .employeeCode(trainer.getEmployeeCode())
                .name(trainer.getName())
                .mobile(trainer.getMobile())
                .email(trainer.getEmail())
                .specialization(trainer.getSpecialization())
                .qualification(trainer.getQualification())
                .experienceYears(trainer.getExperienceYears())
                .joiningDate(trainer.getJoiningDate())
                .status(trainer.getStatus().name())
                .createdAt(trainer.getCreatedAt())
                .updatedAt(trainer.getUpdatedAt())
                .build();
    }
}
