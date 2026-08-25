package com.trainingcenter.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CourseRequest {

    @NotNull(message = "Center ID is required")
    private Long centerId;

    @NotBlank(message = "Course code is required")
    @Size(max = 30, message = "Course code must be under 30 characters")
    private String courseCode;

    @NotBlank(message = "Course name is required")
    @Size(max = 100, message = "Course name must be under 100 characters")
    private String courseName;

    private String description;

    @Size(max = 50, message = "Duration must be under 50 characters")
    private String duration;

    @NotNull(message = "Total fee is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Total fee must be greater than zero")
    private BigDecimal totalFee;

    @NotBlank(message = "Status is required")
    private String status; // ACTIVE, INACTIVE
}
