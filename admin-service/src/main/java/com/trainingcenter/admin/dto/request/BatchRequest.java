package com.trainingcenter.admin.dto.request;

import com.trainingcenter.admin.enums.CourseStatus;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
public class BatchRequest {

    @NotBlank(message = "Batch code is required")
    @Size(max = 20, message = "Batch code must not exceed 20 characters")
    private String batchCode;

    @NotBlank(message = "Batch name is required")
    @Size(max = 100, message = "Batch name must not exceed 100 characters")
    private String batchName;

    @NotNull(message = "Course ID is required")
    private Long courseId;

    @NotNull(message = "Center ID is required")
    private Long centerId;

    private Long trainerId;

    @NotNull(message = "Start date is required")
    private LocalDate startDate;

    private LocalDate endDate;

    @Size(max = 50, message = "Timings must not exceed 50 characters")
    private String timings;

    @NotNull(message = "Max students is required")
    @Min(value = 1, message = "Max students must be at least 1")
    private Integer maxStudents;

    private CourseStatus status;
}
