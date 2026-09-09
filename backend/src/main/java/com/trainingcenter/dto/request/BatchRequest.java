package com.trainingcenter.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
public class BatchRequest {

    @NotNull(message = "Course ID is required")
    private Long courseId;

    private Long trainerId; // can be null initially

    @NotNull(message = "Center ID is required")
    private Long centerId;

    @NotBlank(message = "Batch code is required")
    @Size(max = 30, message = "Batch code must be under 30 characters")
    private String batchCode;

    @NotBlank(message = "Batch name is required")
    @Size(max = 100, message = "Batch name must be under 100 characters")
    private String batchName;

    private LocalDate startDate;
    private LocalDate endDate;

    private Integer maxStudents;

    @NotBlank(message = "Status is required")
    private String status; // ACTIVE, INACTIVE
}
