package com.trainingcenter.repository;

import com.trainingcenter.entity.AssessmentResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AssessmentResultRepository extends JpaRepository<AssessmentResult, Long> {

    List<AssessmentResult> findByEnrollmentEnrollmentId(Long enrollmentId);

    List<AssessmentResult> findByAssessmentAssessmentId(Long assessmentId);

    Optional<AssessmentResult> findByAssessmentAssessmentIdAndEnrollmentEnrollmentId(Long assessmentId, Long enrollmentId);

    @Query("SELECT AVG(r.percentage) FROM AssessmentResult r WHERE r.enrollment.enrollmentId = :enrollmentId")
    Double getAveragePercentageByEnrollmentId(@Param("enrollmentId") Long enrollmentId);
}
