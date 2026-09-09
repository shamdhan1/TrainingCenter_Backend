package com.trainingcenter.service.impl;

import com.trainingcenter.dto.request.StudentRequest;
import com.trainingcenter.dto.response.StudentResponse;
import com.trainingcenter.entity.Center;
import com.trainingcenter.entity.Role;
import com.trainingcenter.entity.Student;
import com.trainingcenter.entity.UserAccount;
import com.trainingcenter.enums.RoleName;
import com.trainingcenter.enums.UserStatus;
import com.trainingcenter.repository.CenterRepository;
import com.trainingcenter.repository.RoleRepository;
import com.trainingcenter.repository.StudentRepository;
import com.trainingcenter.repository.UserAccountRepository;
import com.trainingcenter.service.StudentService;
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
public class StudentServiceImpl implements StudentService {

    @Autowired
    private StudentRepository studentRepository;

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
    public Page<StudentResponse> searchStudents(String query, String status, Long centerId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        UserStatus userStatus = null;
        if (status != null && !status.trim().isEmpty()) {
            userStatus = UserStatus.valueOf(status.toUpperCase());
        }
        return studentRepository.searchStudents(query, userStatus, centerId, pageable)
                .map(this::mapToResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public StudentResponse getStudentById(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found with ID: " + id));
        return mapToResponse(student);
    }

    @Override
    @Transactional(readOnly = true)
    public StudentResponse getStudentByUserId(Long userId) {
        Student student = studentRepository.findByUserUserId(userId)
                .orElseThrow(() -> new RuntimeException("Student not found with User ID: " + userId));
        return mapToResponse(student);
    }

    @Override
    @Transactional
    public StudentResponse createStudent(StudentRequest request) {
        logValidation(request.getUsername(), request.getEmail(), request.getRegistrationNo(), null);

        Center center = centerRepository.findById(request.getCenterId())
                .orElseThrow(() -> new RuntimeException("Center not found with ID: " + request.getCenterId()));

        Role role = roleRepository.findByRoleName(RoleName.ROLE_STUDENT)
                .orElseGet(() -> roleRepository.save(Role.builder().roleName(RoleName.ROLE_STUDENT).build()));

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

        // 2. Create Student profile
        Student student = Student.builder()
                .user(savedUser)
                .center(center)
                .registrationNo(request.getRegistrationNo().toUpperCase().trim())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .dateOfBirth(request.getDateOfBirth())
                .gender(request.getGender())
                .mobile(request.getMobile())
                .email(request.getEmail().toLowerCase().trim())
                .address(request.getAddress())
                .qualification(request.getQualification())
                .fatherName(request.getFatherName())
                .motherName(request.getMotherName())
                .guardianName(request.getGuardianName())
                .guardianMobile(request.getGuardianMobile())
                .aadhaarNo(request.getAadhaarNo())
                .city(request.getCity())
                .state(request.getState())
                .pincode(request.getPincode())
                .registrationDate(request.getRegistrationDate() != null ? request.getRegistrationDate()
                        : java.time.LocalDate.now())
                .status(userStatus)
                .build();

        Student savedStudent = studentRepository.save(student);
        return mapToResponse(savedStudent);
    }

    @Override
    @Transactional
    public StudentResponse updateStudent(Long id, StudentRequest request) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found with ID: " + id));

        logValidation(request.getUsername(), request.getEmail(), request.getRegistrationNo(), student);

        Center center = centerRepository.findById(request.getCenterId())
                .orElseThrow(() -> new RuntimeException("Center not found with ID: " + request.getCenterId()));

        UserStatus userStatus = UserStatus.valueOf(request.getStatus().toUpperCase());

        // Update UserAccount
        UserAccount user = student.getUser();
        user.setUsername(request.getUsername().toLowerCase().trim());
        if (request.getPassword() != null && !request.getPassword().trim().isEmpty()
                && !request.getPassword().equals("********")) {
            user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        }
        user.setEmail(request.getEmail().toLowerCase().trim());
        user.setMobile(request.getMobile());
        user.setStatus(userStatus);
        user.setCenter(center);
        userAccountRepository.save(user);

        // Update Student profile
        student.setCenter(center);
        student.setRegistrationNo(request.getRegistrationNo().toUpperCase().trim());
        student.setFirstName(request.getFirstName());
        student.setLastName(request.getLastName());
        student.setDateOfBirth(request.getDateOfBirth());
        student.setGender(request.getGender());
        student.setMobile(request.getMobile());
        student.setEmail(request.getEmail().toLowerCase().trim());
        student.setAddress(request.getAddress());
        student.setQualification(request.getQualification());
        student.setStatus(userStatus);
        if (request.getRegistrationDate() != null) {
            student.setRegistrationDate(request.getRegistrationDate());
        }

        Student updatedStudent = studentRepository.save(student);
        return mapToResponse(updatedStudent);
    }

    @Override
    @Transactional
    public void deleteStudent(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found with ID: " + id));
        // Delete student profile and associated user account cascade
        studentRepository.delete(student);
    }

    private void logValidation(String username, String email, String regNo, Student existingStudent) {
        if (existingStudent == null) {
            // Creation validation
            if (userAccountRepository.existsByUsername(username.toLowerCase().trim())) {
                throw new RuntimeException("Username already exists: " + username);
            }
            if (userAccountRepository.existsByEmail(email.toLowerCase().trim())) {
                throw new RuntimeException("Email already exists: " + email);
            }
            if (studentRepository.existsByRegistrationNo(regNo.toUpperCase().trim())) {
                throw new RuntimeException("Registration number already exists: " + regNo);
            }
        } else {
            // Update validation
            UserAccount existingUser = existingStudent.getUser();
            if (!existingUser.getUsername().equalsIgnoreCase(username.trim()) &&
                    userAccountRepository.existsByUsername(username.toLowerCase().trim())) {
                throw new RuntimeException("Username already exists: " + username);
            }
            if (!existingUser.getEmail().equalsIgnoreCase(email.trim()) &&
                    userAccountRepository.existsByEmail(email.toLowerCase().trim())) {
                throw new RuntimeException("Email already exists: " + email);
            }
            if (!existingStudent.getRegistrationNo().equalsIgnoreCase(regNo.trim()) &&
                    studentRepository.existsByRegistrationNo(regNo.toUpperCase().trim())) {
                throw new RuntimeException("Registration number already exists: " + regNo);
            }
        }
    }

    private StudentResponse mapToResponse(Student student) {
        return StudentResponse.builder()
                .studentId(student.getStudentId())
                .userId(student.getUser().getUserId())
                .username(student.getUser().getUsername())
                .centerId(student.getCenter().getCenterId())
                .centerName(student.getCenter().getName())
                .registrationNo(student.getRegistrationNo())
                .firstName(student.getFirstName())
                .lastName(student.getLastName())
                .dateOfBirth(student.getDateOfBirth())
                .gender(student.getGender())
                .mobile(student.getMobile())
                .email(student.getEmail())
                .address(student.getAddress())
                .fatherName(student.getFatherName())
                .motherName(student.getMotherName())
                .guardianName(student.getGuardianName())
                .guardianMobile(student.getGuardianMobile())
                .aadhaarNo(student.getAadhaarNo())
                .city(student.getCity())
                .state(student.getState())
                .pincode(student.getPincode())
                .qualification(student.getQualification())
                .registrationDate(student.getRegistrationDate())
                .status(student.getStatus().name())
                .createdAt(student.getCreatedAt())
                .updatedAt(student.getUpdatedAt())
                .build();
    }
}
