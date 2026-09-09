package com.trainingcenter.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrainerReportResponse {


    private Long trainerId;
    private String trainerName;
    private String employeeCode;
    private String specialization;
    private String centerName;

    private LocalDate fromDate;
    private LocalDate toDate;


    private long totalStudents;
    private long activeStudents;
    private long passoutStudents;
    private long cancelledStudents;

    private long assessmentsConducted;
    private long coursesTaught;


    // =========================
    // PERFORMANCE
    // =========================

    private double attendancePercentage;
    private double averageMarks;
    private double passRate;
    private double averageProgress;

    private double performanceScore;

    // =========================
    // GROWTH
    // =========================

    private GrowthData studentGrowth;
    private GrowthData passoutGrowth;
    private GrowthData attendanceGrowth;
    private GrowthData passRateGrowth;
    private GrowthData progressGrowth;

    // =========================
    // STUDENT DETAILS
    // =========================

    private List<StudentReportItem> students;


    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GrowthData {

        private double currentValue;
        private double previousValue;
        private Double growthPercentage;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StudentReportItem {

        private Long studentId;
        private String registrationNo;
        private String studentName;
        private String courseName;

        private String status;

        private double attendancePercentage;
        private double averageMarks;
        private double progressPercentage;

        private String result;
    }

}
