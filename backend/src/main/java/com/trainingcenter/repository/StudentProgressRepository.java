package com.trainingcenter.repository;

import com.trainingcenter.entity.StudentProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentProgressRepository extends JpaRepository<StudentProgress, Long> {

    List<StudentProgress> findByEnrollmentEnrollmentId(Long enrollmentId);

    Optional<StudentProgress> findByEnrollmentEnrollmentIdAndCourseContentContentId(Long enrollmentId, Long contentId);

    @Query("SELECT SUM(sp.progressPercent) FROM StudentProgress sp WHERE sp.enrollment.enrollmentId = :enrollmentId")
    Long sumProgressPercentByEnrollmentId(@Param("enrollmentId") Long enrollmentId);

    @Query("""
    SELECT sp
    FROM StudentProgress sp
    WHERE sp.enrollment.enrollmentId = :enrollmentId
""")
    List<StudentProgress> findProgressByEnrollmentId(
            @Param("enrollmentId") Long enrollmentId
    );
}
