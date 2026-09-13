package com.trainingcenter.trainer.service.impl;

import com.trainingcenter.trainer.dto.request.TrainerRegistrationRequest;
import com.trainingcenter.trainer.dto.response.TrainerRegistrationResponse;
import com.trainingcenter.trainer.entity.Trainer;
import com.trainingcenter.trainer.enums.UserStatus;
import com.trainingcenter.trainer.exception.ConflictException;
import com.trainingcenter.trainer.repository.TrainerRepository;
import com.trainingcenter.trainer.service.TrainerRegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TrainerRegistrationServiceImpl implements TrainerRegistrationService {

    private final TrainerRepository trainerRepository;

    @Override
    @Transactional(readOnly = true)
    public List<TrainerRegistrationResponse> getAllTrainer() {
        return trainerRepository.findAll().stream()
                .map(this::mapToRegistrationResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public TrainerRegistrationResponse registerTrainer(TrainerRegistrationRequest request) {
        if (trainerRepository.existsByEmployeeCode(request.getEmployeeCode())) {
            throw new ConflictException("Trainer with employee code already exists: " + request.getEmployeeCode());
        }

        Trainer trainer = Trainer.builder()
                .centerId(request.getCenterId())
                .employeeCode(request.getEmployeeCode().toUpperCase())
                .name(request.getName())
                .email(request.getEmail())
                .mobile(request.getMobile())
                .specialization(request.getSpecialization())
                .qualification(request.getQualification())
                .experienceYears(request.getExperienceYears())
                .joiningDate(request.getJoiningDate())
                .status(request.getStatus() != null ? UserStatus.valueOf(request.getStatus().toUpperCase()) : UserStatus.ACTIVE)
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
                .contractType(request.getContractType())
                .salary(request.getSalary())
                .bankName(request.getBankName())
                .bankAccountNumber(request.getBankAccountNumber())
                .ifscCode(request.getIfscCode())
                .bio(request.getBio())
                .build();

        Trainer savedTrainer = trainerRepository.save(trainer);
        TrainerRegistrationResponse response = mapToRegistrationResponse(savedTrainer);
        response.setMessage("Trainer registered successfully");
        return response;
    }

    private TrainerRegistrationResponse mapToRegistrationResponse(Trainer trainer) {
        return TrainerRegistrationResponse.builder()
                .trainerId(trainer.getTrainerId())
                .userId(trainer.getUserId())
                .employeeCode(trainer.getEmployeeCode())
                .name(trainer.getName())
                .email(trainer.getEmail())
                .status(trainer.getStatus() != null ? trainer.getStatus().name() : "ACTIVE")
                .gender(trainer.getGender())
                .dateOfBirth(trainer.getDateOfBirth())
                .aadhaarNo(trainer.getAadhaarNo())
                .panNo(trainer.getPanNo())
                .alternativeMobile(trainer.getAlternativeMobile())
                .address(trainer.getAddress())
                .city(trainer.getCity())
                .state(trainer.getState())
                .pincode(trainer.getPincode())
                .designation(trainer.getDesignation())
                .contractType(trainer.getContractType())
                .salary(trainer.getSalary())
                .bankName(trainer.getBankName())
                .bankAccountNumber(trainer.getBankAccountNumber())
                .ifscCode(trainer.getIfscCode())
                .bio(trainer.getBio())
                .build();
    }
}
