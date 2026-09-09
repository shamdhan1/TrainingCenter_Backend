package com.trainingcenter.repository;

import com.trainingcenter.entity.Enrollment;
import com.trainingcenter.enums.EnrollmentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

    boolean existsByStudentStudentIdAndCourseCourseIdAndStatus(Long studentId, Long courseId, EnrollmentStatus status);

    List<Enrollment> findByStudentStudentId(Long studentId);

    List<Enrollment> findByTrainerTrainerId(Long trainerId);

    List<Enrollment> findByTrainerIsNullAndCenterCenterId(Long centerId);

    long countByTrainerIsNullAndCenterCenterId(Long centerId);

    long countByCenterCenterId(Long centerId);

    @Query("SELECT COUNT(e) FROM Enrollment e WHERE e.center.centerId = :centerId AND e.status = 'ACTIVE'")
    long countActiveByCenterCenterId(@Param("centerId") Long centerId);

    @Query("SELECT e FROM Enrollment e WHERE " +
           "(:studentQuery IS NULL OR :studentQuery = '' OR " +
           " LOWER(e.student.firstName) LIKE LOWER(CONCAT('%', :studentQuery, '%')) OR " +
           " LOWER(e.student.lastName) LIKE LOWER(CONCAT('%', :studentQuery, '%')) OR " +
           " LOWER(e.student.registrationNo) LIKE LOWER(CONCAT('%', :studentQuery, '%'))) AND " +
           "(:courseId IS NULL OR e.course.courseId = :courseId) AND " +
           "(:trainerId IS NULL OR e.trainer.trainerId = :trainerId) AND " +
           "(:centerId IS NULL OR e.center.centerId = :centerId) AND " +
           "(:status IS NULL OR e.status = :status)")
    Page<Enrollment> searchEnrollments(@Param("studentQuery") String studentQuery,
                                       @Param("courseId") Long courseId,
                                       @Param("trainerId") Long trainerId,
                                       @Param("centerId") Long centerId,
                                       @Param("status") EnrollmentStatus status,
                                       Pageable pageable);


    List<Enrollment> findByTrainerTrainerIdAndRegistrationDateBetween(
            long Triner_id,
            LocalDate fromDate,
            LocalDate endDate
    );

    List<Enrollment> findByTrainerTrainerIdAndActualEndDateBetween(
            Long trainerId,
            LocalDate fromDate,
            LocalDate toDate
    );

}
