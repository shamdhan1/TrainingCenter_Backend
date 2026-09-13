package com.trainingcenter.student.repository;

import com.trainingcenter.student.entity.StudentProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentProgressRepository extends JpaRepository<StudentProgress, Long> {
    List<StudentProgress> findByEnrollmentEnrollmentId(Long enrollmentId);
}
