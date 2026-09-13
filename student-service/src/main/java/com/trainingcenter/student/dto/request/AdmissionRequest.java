package com.trainingcenter.student.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class AdmissionRequest {

    private String username;
    private String password;

    @NotNull(message = "Center ID is required")
    private Long centerId;

    @NotBlank(message = "Registration number is required")
    @Size(max = 30)
    private String registrationNo;

    @NotBlank(message = "First name is required")
    @Size(max = 50)
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(max = 50)
    private String lastName;

    private LocalDate dateOfBirth;
    private String gender;

    @NotBlank(message = "Mobile is required")
    @Size(max = 15)
    private String mobile;

    @NotBlank(message = "Email is required")
    @Email
    private String email;

    private String qualification;
    private String address;
    private String city;
    private String state;
    private String pincode;

    private String fatherName;
    private String motherName;
    private String guardianName;
    private String guardianMobile;
    private String aadhaarNo;

    private LocalDate registrationDate;
    private String status;

    @NotNull(message = "Course ID is required")
    private Long courseId;

    private Long batchId;
    private Long trainerId;
    private LocalDate startDate;
    private LocalDate expectedEndDate;

    private BigDecimal totalFee = BigDecimal.valueOf(10000);
    private BigDecimal discount = BigDecimal.ZERO;
    private BigDecimal initialPayment = BigDecimal.ZERO;
    private String paymentMode;
    private String transactionReference;
    private LocalDate feeDueDate;
    private String remarks;
}
