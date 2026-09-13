package com.trainingcenter.student.service;

import com.trainingcenter.student.dto.request.StudentRequest;
import com.trainingcenter.student.dto.response.StudentResponse;
import org.springframework.data.domain.Page;

public interface StudentService {
    Page<StudentResponse> searchStudents(String query, String status, Long centerId, int page, int size);
    StudentResponse getStudentById(Long id);
    StudentResponse createStudent(StudentRequest request);
    StudentResponse updateStudent(Long id, StudentRequest request);
    void deleteStudent(Long id);
    long countStudents();
}
