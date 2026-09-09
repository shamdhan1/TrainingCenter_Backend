package com.trainingcenter.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class EnrollmentRequest {

    @NotNull(message = "Student ID is required")
    private Long studentId;

    @NotNull(message = "Course ID is required")
    private Long courseId;

    private Long trainerId; // can be null initially

    @NotNull(message = "Center ID is required")
    private Long centerId;

    private LocalDate registrationDate;
    private LocalDate startDate;
    private LocalDate expectedEndDate;
    private String remarks;

    // Parameters to auto-generate the FeeAccount
    @DecimalMin(value = "0.0", message = "Discount cannot be negative")
    private BigDecimal discount = BigDecimal.ZERO;

    private LocalDate feeDueDate;
}
