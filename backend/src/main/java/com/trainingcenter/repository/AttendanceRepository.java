package com.trainingcenter.repository;

import com.trainingcenter.entity.Attendance;
import com.trainingcenter.enums.AttendanceStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    List<Attendance> findByEnrollmentEnrollmentId(Long enrollmentId);

    List<Attendance> findByTrainerTrainerId(Long trainerId);

    Optional<Attendance> findByEnrollmentEnrollmentIdAndAttendanceDate(Long enrollmentId, LocalDate attendanceDate);

    boolean existsByEnrollmentEnrollmentIdAndAttendanceDate(Long enrollmentId, LocalDate attendanceDate);

    long countByEnrollmentEnrollmentId(Long enrollmentId);

    long countByEnrollmentEnrollmentIdAndStatus(Long enrollmentId, AttendanceStatus status);

    long countByAttendanceDate(LocalDate attendanceDate);

    long countByAttendanceDateAndEnrollmentCenterCenterId(LocalDate attendanceDate, Long centerId);


    List<Attendance> findByTrainerTrainerIdAndAttendanceDateBetween(
            Long trainerId,
            LocalDate fromDate,
            LocalDate toDate
    );

}
