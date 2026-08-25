package com.trainingcenter.service;

import com.trainingcenter.dto.request.StudentRequest;
import com.trainingcenter.dto.response.StudentResponse;
import org.springframework.data.domain.Page;

public interface StudentService {
    Page<StudentResponse> searchStudents(String query, String status, Long centerId, int page, int size);
    StudentResponse getStudentById(Long id);
    StudentResponse getStudentByUserId(Long userId);
    StudentResponse createStudent(StudentRequest request);
    StudentResponse updateStudent(Long id, StudentRequest request);
    void deleteStudent(Long id);
}
