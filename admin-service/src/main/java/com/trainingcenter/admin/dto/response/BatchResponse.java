package com.trainingcenter.admin.dto.response;

import com.trainingcenter.admin.enums.CourseStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BatchResponse {
    private Long batchId;
    private String batchCode;
    private String batchName;
    private Long courseId;
    private String courseName;
    private Long centerId;
    private String centerName;
    private Long trainerId;
    private String trainerName;
    private LocalDate startDate;
    private LocalDate endDate;
    private String timings;
    private Integer maxStudents;
    private CourseStatus status;
}
