package com.trainingcenter.service.impl;


import com.trainingcenter.dto.response.TrainerReportResponse;
import com.trainingcenter.entity.*;
import com.trainingcenter.enums.AttendanceStatus;
import com.trainingcenter.enums.EnrollmentStatus;
import com.trainingcenter.repository.*;
import com.trainingcenter.service.TrainerReportService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class TrainerReportServiceImpl implements TrainerReportService {


    private final TrainerRepository trainerRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final AttendanceRepository attendanceRepository;
    private final AssessmentResultRepository assessmentResultRepository;
    private final StudentProgressRepository studentProgressRepository;


    @Override
    public TrainerReportResponse generateReport(Long trainerId,
                                                LocalDate fromDate,
                                                LocalDate toDate) {

        // error handling
        if (fromDate == null || toDate == null) throw
                new RuntimeException("From date and To date are required");

        if (fromDate.isAfter(toDate)) throw
                new RuntimeException("From date cannot be after To date");

        Trainer trainerAreNotFound = trainerRepository.findById(trainerId).orElseThrow(()->
                new RuntimeException("trainer are not found"));

        // get data into service class

        List<Enrollment> currentEnrollments = enrollmentRepository
                .findByTrainerTrainerIdAndRegistrationDateBetween(trainerId, fromDate, toDate);

        List<Enrollment> completedEnrollments = enrollmentRepository
                        .findByTrainerTrainerIdAndActualEndDateBetween(trainerId,fromDate,toDate);

        List<Attendance> currentAttendance = attendanceRepository
                .findByTrainerTrainerIdAndAttendanceDateBetween(trainerId, fromDate, toDate);

        List<AssessmentResult> currentResults = assessmentResultRepository
                .findByTrainerAndDateRange(trainerId, fromDate, toDate);

        long totalStudents = currentEnrollments
                .stream()
                .map(e -> e.getStudent().getStudentId())
                .distinct()
                .count();

        long activeStudents = currentEnrollments
                .stream()
                .filter(e -> e.getStatus() == EnrollmentStatus.ACTIVE)
                .map(e -> e.getStudent().getStudentId())
                .distinct()
                .count();

        long cancelledStudents = currentEnrollments
                .stream()
                .filter(e -> e.getStatus() == EnrollmentStatus.CANCELLED)
                .map(e -> e.getStudent().getStudentId())
                .distinct()
                .count();

        long passoutStudents = completedEnrollments
                .stream()
                .map(e -> e.getStudent().getStudentId())
                .distinct()
                .count();

        // ==========================================
        // ATTENDANCE
        // ==========================================

        double attendancePercentage =
                calculateAttendance(currentAttendance);

        // ==========================================
        // ASSESSMENTS
        // ==========================================

        double averageMarks =
                currentResults
                        .stream()
                        .map(AssessmentResult::getPercentage)
                        .filter(Objects::nonNull)
                        .mapToDouble(Double::doubleValue)
                        .average()
                        .orElse(0.0);

        long passedResults =
                currentResults
                        .stream()
                        .filter(r ->
                                "PASS".equalsIgnoreCase(r.getPassFail())
                        )
                        .count();

        double passRate = currentResults.isEmpty()
                ? 0.0
                : (passedResults * 100.0) / currentResults.size();

        // ==========================================
        // PROGRESS
        // ==========================================

        double averageProgress =
                calculateAverageProgress(currentEnrollments);

        // ==========================================
        // PERFORMANCE SCORE
        // ==========================================

        double performanceScore =
                calculatePerformanceScore(
                        attendancePercentage,
                        passRate,
                        averageProgress,
                        averageMarks
                );

        // ==========================================
        // COURSES
        // ==========================================

        long coursesTaught = currentEnrollments
                .stream()
                .map(e -> e.getCourse().getCourseId())
                .distinct()
                .count();

        // ==========================================
        // PREVIOUS PERIOD
        // ==========================================

        long periodDays =
                ChronoUnit.DAYS.between(fromDate, toDate) + 1;

        LocalDate previousTo =
                fromDate.minusDays(1);

        LocalDate previousFrom =
                previousTo.minusDays(periodDays - 1);

        List<Enrollment> previousEnrollments =
                enrollmentRepository
                        .findByTrainerTrainerIdAndRegistrationDateBetween(
                                trainerId,
                                previousFrom,
                                previousTo
                        );

        List<Enrollment> previousCompleted =
                enrollmentRepository
                        .findByTrainerTrainerIdAndActualEndDateBetween(
                                trainerId,
                                previousFrom,
                                previousTo
                        );

        List<Attendance> previousAttendanceList  =
                attendanceRepository
                        .findByTrainerTrainerIdAndAttendanceDateBetween(
                                trainerId,
                                previousFrom,
                                previousTo
                        );

        List<AssessmentResult> previousResults =
                assessmentResultRepository
                        .findByTrainerAndDateRange(
                                trainerId,
                                previousFrom,
                                previousTo
                        );

        double previousAttendance =
                calculateAttendance(previousAttendanceList);

        double previousPassRate =
                calculatePassRate(previousResults);

        double previousProgress =
                calculateAverageProgress(previousEnrollments);

        long previousStudents =
                previousEnrollments
                        .stream()
                        .map(e -> e.getStudent().getStudentId())
                        .distinct()
                        .count();

        long previousPassouts =
                previousCompleted
                        .stream()
                        .map(e -> e.getStudent().getStudentId())
                        .distinct()
                        .count();

        // ==========================================
        // STUDENT DETAILS
        // ==========================================

        List<TrainerReportResponse.StudentReportItem> studentItems =
                currentEnrollments
                        .stream()
                        .collect(Collectors.toMap(
                                Enrollment::getEnrollmentId,
                                e -> createStudentReport(e, currentAttendance),
                                (a, b) -> a
                        ))
                        .values()
                        .stream()
                        .toList();

        // ==========================================
        // RESPONSE
        // ==========================================

        Trainer trainer = new Trainer();
        return TrainerReportResponse.builder()

                .trainerId(trainer.getTrainerId())
                .trainerName(trainer.getName())
                .employeeCode(trainer.getEmployeeCode())
                .specialization(trainer.getSpecialization())
                .centerName(
                        trainer.getCenter() != null
                                ? trainer.getCenter().getName()
                                : null
                )

                .fromDate(fromDate)
                .toDate(toDate)

                .totalStudents(totalStudents)
                .activeStudents(activeStudents)
                .passoutStudents(passoutStudents)
                .cancelledStudents(cancelledStudents)

                .assessmentsConducted(currentResults.size())
                .coursesTaught(coursesTaught)

                .attendancePercentage(round(attendancePercentage))
                .averageMarks(round(averageMarks))
                .passRate(round(passRate))
                .averageProgress(round(averageProgress))

                .performanceScore(round(performanceScore))

                .studentGrowth(
                        growth(totalStudents, previousStudents)
                )

                .passoutGrowth(
                        growth(passoutStudents, previousPassouts)
                )

                .attendanceGrowth(
                        growth(attendancePercentage, previousAttendance)
                )

                .passRateGrowth(
                        growth(passRate, previousPassRate)
                )

                .progressGrowth(
                        growth(averageProgress, previousProgress)
                )

                .students(studentItems)

                .build();
    }

    // ==========================================
    // ATTENDANCE CALCULATION
    // ==========================================

    private double calculateAttendance(
            List<Attendance> attendance) {

        if (attendance.isEmpty()) {
            return 0.0;
        }

        long considered =
                attendance.stream()
                        .filter(a ->
                                a.getStatus() == AttendanceStatus.PRESENT ||
                                        a.getStatus() == AttendanceStatus.ABSENT ||
                                        a.getStatus() == AttendanceStatus.LATE
                        )
                        .count();

        if (considered == 0) {
            return 0.0;
        }

        long present =
                attendance.stream()
                        .filter(a ->
                                a.getStatus() == AttendanceStatus.PRESENT ||
                                        a.getStatus() == AttendanceStatus.LATE
                        )
                        .count();

        return present * 100.0 / considered;
    }

    // ==========================================
    // PASS RATE
    // ==========================================

    private double calculatePassRate(
            List<AssessmentResult> results) {

        if (results.isEmpty()) {
            return 0.0;
        }

        long passed =
                results.stream()
                        .filter(r ->
                                "PASS".equalsIgnoreCase(
                                        r.getPassFail()
                                )
                        )
                        .count();

        return passed * 100.0 / results.size();
    }

    // ==========================================
    // PROGRESS
    // ==========================================

    private double calculateAverageProgress(
            List<Enrollment> enrollments) {

        List<Double> progressValues = new ArrayList<>();

        for (Enrollment enrollment : enrollments) {

            List<StudentProgress> progress =
                    studentProgressRepository
                            .findByEnrollmentEnrollmentId(
                                    enrollment.getEnrollmentId()
                            );

            if (!progress.isEmpty()) {

                double avg =
                        progress.stream()
                                .map(StudentProgress::getProgressPercent)
                                .filter(Objects::nonNull)
                                .mapToInt(Integer::intValue)
                                .average()
                                .orElse(0.0);

                progressValues.add(avg);
            }
        }

        return progressValues.stream()
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(0.0);
    }

    // ==========================================
    // PERFORMANCE SCORE
    // ==========================================

    private double calculatePerformanceScore(
            double attendance,
            double passRate,
            double progress,
            double averageMarks) {

        /*
         * Weight:
         *
         * Attendance = 25%
         * Pass Rate  = 30%
         * Progress   = 25%
         * Marks      = 20%
         */

        return
                (attendance * 0.25) +
                        (passRate * 0.30) +
                        (progress * 0.25) +
                        (averageMarks * 0.20);
    }

    // ==========================================
    // STUDENT REPORT
    // ==========================================

    private TrainerReportResponse.StudentReportItem
    createStudentReport(
            Enrollment enrollment,
            List<Attendance> allAttendance) {

        Long enrollmentId =
                enrollment.getEnrollmentId();

        List<Attendance> attendance =
                allAttendance.stream()
                        .filter(a ->
                                a.getEnrollment()
                                        .getEnrollmentId()
                                        .equals(enrollmentId)
                        )
                        .toList();

        double attendancePercentage =
                calculateAttendance(attendance);

        List<AssessmentResult> results =
                assessmentResultRepository
                        .findByEnrollmentEnrollmentId(
                                enrollmentId
                        );

        double averageMarks =
                results.stream()
                        .map(AssessmentResult::getPercentage)
                        .filter(Objects::nonNull)
                        .mapToDouble(Double::doubleValue)
                        .average()
                        .orElse(0.0);

        List<StudentProgress> progress =
                studentProgressRepository
                        .findByEnrollmentEnrollmentId(
                                enrollmentId
                        );

        double progressPercentage =
                progress.stream()
                        .map(StudentProgress::getProgressPercent)
                        .filter(Objects::nonNull)
                        .mapToInt(Integer::intValue)
                        .average()
                        .orElse(0.0);

        String result = "N/A";

        if (!results.isEmpty()) {

            boolean passed =
                    results.stream()
                            .anyMatch(r ->
                                    "PASS".equalsIgnoreCase(
                                            r.getPassFail()
                                    )
                            );

            result = passed ? "PASS" : "FAIL";
        }

        return TrainerReportResponse.StudentReportItem.builder()

                .studentId(
                        enrollment.getStudent().getStudentId()
                )

                .registrationNo(
                        enrollment.getStudent().getRegistrationNo()
                )

                .studentName(
                        enrollment.getStudent().getFirstName()
                                + " "
                                + enrollment.getStudent().getLastName()
                )

                .courseName(
                        enrollment.getCourse().getCourseName()
                )

                .status(
                        enrollment.getStatus().name()
                )

                .attendancePercentage(
                        round(attendancePercentage)
                )

                .averageMarks(
                        round(averageMarks)
                )

                .progressPercentage(
                        round(progressPercentage)
                )

                .result(result)

                .build();
    }

    // ==========================================
    // GROWTH
    // ==========================================

    private TrainerReportResponse.GrowthData growth(
            double current,
            double previous) {

        Double percentage = null;

        if (previous != 0) {
            percentage =
                    ((current - previous) / previous) * 100.0;
        }

        return TrainerReportResponse.GrowthData.builder()
                .currentValue(round(current))
                .previousValue(round(previous))
                .growthPercentage(
                        percentage == null
                                ? null
                                : round(percentage)
                )
                .build();
    }

    private double round(double value) {
        return Math.round(value * 100.0) / 100.0;
    }

}
