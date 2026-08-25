package com.trainingcenter.service;

import com.trainingcenter.dto.request.EnrollmentRequest;
import com.trainingcenter.dto.response.EnrollmentResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface EnrollmentService {
    EnrollmentResponse enrollStudent(EnrollmentRequest request);
    EnrollmentResponse assignTrainer(Long enrollmentId, Long trainerId);
    Page<EnrollmentResponse> searchEnrollments(String studentQuery, Long courseId, Long trainerId, Long centerId, String status, int page, int size);
    EnrollmentResponse getEnrollmentById(Long id);
    List<EnrollmentResponse> getEnrollmentsByStudentId(Long studentId);
    List<EnrollmentResponse> getEnrollmentsByTrainerId(Long trainerId);
    List<EnrollmentResponse> getPendingTrainerAssignments(Long centerId);
}
