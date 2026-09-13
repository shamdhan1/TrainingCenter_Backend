package com.trainingcenter.student.service.impl;

import com.trainingcenter.student.dto.request.StudentRequest;
import com.trainingcenter.student.dto.response.StudentResponse;
import com.trainingcenter.student.entity.Student;
import com.trainingcenter.student.enums.UserStatus;
import com.trainingcenter.student.exception.ConflictException;
import com.trainingcenter.student.exception.ResourceNotFoundException;
import com.trainingcenter.student.repository.StudentRepository;
import com.trainingcenter.student.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;

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
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with ID: " + id));
        return mapToResponse(student);
    }

    @Override
    @Transactional
    public StudentResponse createStudent(StudentRequest request) {
        if (studentRepository.existsByRegistrationNo(request.getRegistrationNo())) {
            throw new ConflictException("Registration number already exists: " + request.getRegistrationNo());
        }

        Student student = Student.builder()
                .centerId(request.getCenterId())
                .registrationNo(request.getRegistrationNo().toUpperCase())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .dateOfBirth(request.getDateOfBirth())
                .gender(request.getGender())
                .mobile(request.getMobile())
                .email(request.getEmail())
                .address(request.getAddress())
                .city(request.getCity())
                .state(request.getState())
                .pincode(request.getPincode())
                .parentName(request.getFatherName() != null ? request.getFatherName() : request.getGuardianName())
                .parentMobile(request.getGuardianMobile())
                .qualification(request.getQualification())
                .admissionDate(request.getRegistrationDate() != null ? request.getRegistrationDate() : LocalDate.now())
                .status(request.getStatus() != null ? UserStatus.valueOf(request.getStatus().toUpperCase()) : UserStatus.ACTIVE)
                .aadhaarNo(request.getAadhaarNo())
                .build();

        return mapToResponse(studentRepository.save(student));
    }

    @Override
    @Transactional
    public StudentResponse updateStudent(Long id, StudentRequest request) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with ID: " + id));

        if (!student.getRegistrationNo().equalsIgnoreCase(request.getRegistrationNo()) &&
                studentRepository.existsByRegistrationNo(request.getRegistrationNo())) {
            throw new ConflictException("Registration number already exists: " + request.getRegistrationNo());
        }

        student.setCenterId(request.getCenterId());
        student.setRegistrationNo(request.getRegistrationNo().toUpperCase());
        student.setFirstName(request.getFirstName());
        student.setLastName(request.getLastName());
        student.setDateOfBirth(request.getDateOfBirth());
        student.setGender(request.getGender());
        student.setMobile(request.getMobile());
        student.setEmail(request.getEmail());
        student.setAddress(request.getAddress());
        student.setCity(request.getCity());
        student.setState(request.getState());
        student.setPincode(request.getPincode());
        student.setParentName(request.getFatherName() != null ? request.getFatherName() : request.getGuardianName());
        student.setParentMobile(request.getGuardianMobile());
        student.setQualification(request.getQualification());
        if (request.getStatus() != null) {
            student.setStatus(UserStatus.valueOf(request.getStatus().toUpperCase()));
        }
        student.setAadhaarNo(request.getAadhaarNo());

        return mapToResponse(studentRepository.save(student));
    }

    @Override
    @Transactional
    public void deleteStudent(Long id) {
        if (!studentRepository.existsById(id)) {
            throw new ResourceNotFoundException("Student not found with ID: " + id);
        }
        studentRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public long countStudents() {
        return studentRepository.count();
    }

    private StudentResponse mapToResponse(Student student) {
        return StudentResponse.builder()
                .studentId(student.getStudentId())
                .userId(student.getUserId())
                .centerId(student.getCenterId())
                .registrationNo(student.getRegistrationNo())
                .firstName(student.getFirstName())
                .lastName(student.getLastName())
                .dateOfBirth(student.getDateOfBirth())
                .gender(student.getGender())
                .mobile(student.getMobile())
                .email(student.getEmail())
                .address(student.getAddress())
                .qualification(student.getQualification())
                .fatherName(student.getParentName())
                .guardianName(student.getParentName())
                .guardianMobile(student.getParentMobile())
                .aadhaarNo(student.getAadhaarNo())
                .city(student.getCity())
                .state(student.getState())
                .pincode(student.getPincode())
                .registrationDate(student.getAdmissionDate())
                .status(student.getStatus() != null ? student.getStatus().name() : "ACTIVE")
                .createdAt(student.getCreatedAt())
                .updatedAt(student.getUpdatedAt())
                .build();
    }
}
