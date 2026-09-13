package com.trainingcenter.student.repository;

import com.trainingcenter.student.entity.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    List<Attendance> findByEnrollmentEnrollmentId(Long enrollmentId);
    List<Attendance> findByTrainerIdAndAttendanceDate(Long trainerId, LocalDate attendanceDate);
}
