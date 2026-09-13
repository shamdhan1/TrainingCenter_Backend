package com.trainingcenter.student.repository;

import com.trainingcenter.student.entity.Enrollment;
import com.trainingcenter.student.enums.EnrollmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    List<Enrollment> findByStudentStudentId(Long studentId);
    List<Enrollment> findByBatchId(Long batchId);
    List<Enrollment> findByCourseId(Long courseId);
    List<Enrollment> findByTrainerId(Long trainerId);
    List<Enrollment> findByCenterId(Long centerId);
    Optional<Enrollment> findByStudentStudentIdAndBatchId(Long studentId, Long batchId);
    long countByStatus(EnrollmentStatus status);
}
