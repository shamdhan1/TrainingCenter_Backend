package com.trainingcenter.repository;

import com.trainingcenter.entity.Assessment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssessmentRepository extends JpaRepository<Assessment, Long> {
    List<Assessment> findByEnrollmentEnrollmentId(Long enrollmentId);
    List<Assessment> findByTrainerTrainerId(Long trainerId);
}
