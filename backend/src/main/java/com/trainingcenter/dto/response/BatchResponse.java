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
public class BatchResponse {
    private Long batchId;
    private Long courseId;
    private String courseName;
    private Long trainerId;
    private String trainerName;
    private Long centerId;
    private String centerName;
    private String batchCode;
    private String batchName;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer maxStudents;
    private String status;
    private LocalDateTime createdAt;
}
