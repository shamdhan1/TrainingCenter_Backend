package com.trainingcenter.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EnrollmentResponse {
    private Long enrollmentId;
    private Long studentId;
    private String studentName;
    private String studentRegNo;
    private Long courseId;
    private String courseName;
    private Long trainerId;
    private String trainerName;
    private Long centerId;
    private String centerName;
    private LocalDate registrationDate;
    private LocalDate startDate;
    private LocalDate expectedEndDate;
    private LocalDate actualEndDate;
    private String status;
    private String remarks;
    private LocalDateTime createdAt;
}
