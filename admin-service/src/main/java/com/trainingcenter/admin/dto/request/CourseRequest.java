package com.trainingcenter.admin.dto.request;

import com.trainingcenter.admin.enums.CourseStatus;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CourseRequest {

    @NotBlank(message = "Course code is required")
    @Size(max = 20, message = "Course code must not exceed 20 characters")
    private String courseCode;

    @NotBlank(message = "Course name is required")
    @Size(max = 100, message = "Course name must not exceed 100 characters")
    private String courseName;

    private String description;

    @NotNull(message = "Duration in weeks is required")
    @Min(value = 1, message = "Duration must be at least 1 week")
    private Integer durationWeeks;

    @NotNull(message = "Fees are required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Fees must be greater than 0")
    private BigDecimal fees;

    private CourseStatus status;

    private Long centerId;
}
