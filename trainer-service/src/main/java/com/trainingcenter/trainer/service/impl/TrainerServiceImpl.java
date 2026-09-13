package com.trainingcenter.trainer.service.impl;

import com.trainingcenter.trainer.dto.request.TrainerRequest;
import com.trainingcenter.trainer.dto.response.TrainerResponse;
import com.trainingcenter.trainer.entity.Trainer;
import com.trainingcenter.trainer.enums.UserStatus;
import com.trainingcenter.trainer.exception.ConflictException;
import com.trainingcenter.trainer.exception.ResourceNotFoundException;
import com.trainingcenter.trainer.repository.TrainerRepository;
import com.trainingcenter.trainer.service.TrainerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TrainerServiceImpl implements TrainerService {

    private final TrainerRepository trainerRepository;

    @Override
    @Transactional(readOnly = true)
    public List<TrainerResponse> getAllTrainers() {
        return trainerRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

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
                .orElseThrow(() -> new ResourceNotFoundException("Trainer not found with ID: " + id));
        return mapToResponse(trainer);
    }

    @Override
    @Transactional
    public TrainerResponse createTrainer(TrainerRequest request) {
        if (trainerRepository.existsByEmployeeCode(request.getEmployeeCode())) {
            throw new ConflictException("Employee code already exists: " + request.getEmployeeCode());
        }

        Trainer trainer = Trainer.builder()
                .centerId(request.getCenterId())
                .employeeCode(request.getEmployeeCode().toUpperCase())
                .name(request.getName())
                .mobile(request.getMobile())
                .email(request.getEmail())
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

        return mapToResponse(trainerRepository.save(trainer));
    }

    @Override
    @Transactional
    public TrainerResponse updateTrainer(Long id, TrainerRequest request) {
        Trainer trainer = trainerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Trainer not found with ID: " + id));

        if (!trainer.getEmployeeCode().equalsIgnoreCase(request.getEmployeeCode()) &&
                trainerRepository.existsByEmployeeCode(request.getEmployeeCode())) {
            throw new ConflictException("Employee code already exists: " + request.getEmployeeCode());
        }

        trainer.setCenterId(request.getCenterId());
        trainer.setEmployeeCode(request.getEmployeeCode().toUpperCase());
        trainer.setName(request.getName());
        trainer.setMobile(request.getMobile());
        trainer.setEmail(request.getEmail());
        trainer.setSpecialization(request.getSpecialization());
        trainer.setQualification(request.getQualification());
        trainer.setExperienceYears(request.getExperienceYears());
        trainer.setJoiningDate(request.getJoiningDate());
        if (request.getStatus() != null) {
            trainer.setStatus(UserStatus.valueOf(request.getStatus().toUpperCase()));
        }
        trainer.setGender(request.getGender());
        trainer.setDateOfBirth(request.getDateOfBirth());
        trainer.setAadhaarNo(request.getAadhaarNo());
        trainer.setPanNo(request.getPanNo());
        trainer.setAlternativeMobile(request.getAlternativeMobile());
        trainer.setAddress(request.getAddress());
        trainer.setCity(request.getCity());
        trainer.setState(request.getState());
        trainer.setPincode(request.getPincode());
        trainer.setDesignation(request.getDesignation());
        trainer.setContractType(request.getContractType());
        trainer.setSalary(request.getSalary());
        trainer.setBankName(request.getBankName());
        trainer.setBankAccountNumber(request.getBankAccountNumber());
        trainer.setIfscCode(request.getIfscCode());
        trainer.setBio(request.getBio());

        return mapToResponse(trainerRepository.save(trainer));
    }

    @Override
    @Transactional
    public void deleteTrainer(Long id) {
        if (!trainerRepository.existsById(id)) {
            throw new ResourceNotFoundException("Trainer not found with ID: " + id);
        }
        trainerRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public long countTrainers() {
        return trainerRepository.count();
    }

    private TrainerResponse mapToResponse(Trainer trainer) {
        return TrainerResponse.builder()
                .trainerId(trainer.getTrainerId())
                .userId(trainer.getUserId())
                .centerId(trainer.getCenterId())
                .employeeCode(trainer.getEmployeeCode())
                .name(trainer.getName())
                .mobile(trainer.getMobile())
                .email(trainer.getEmail())
                .specialization(trainer.getSpecialization())
                .qualification(trainer.getQualification())
                .experienceYears(trainer.getExperienceYears())
                .joiningDate(trainer.getJoiningDate())
                .status(trainer.getStatus() != null ? trainer.getStatus().name() : "ACTIVE")
                .createdAt(trainer.getCreatedAt())
                .updatedAt(trainer.getUpdatedAt())
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
