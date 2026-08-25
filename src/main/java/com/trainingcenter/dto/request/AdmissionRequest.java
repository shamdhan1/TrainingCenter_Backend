package com.trainingcenter.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdmissionRequest {

    @NotBlank(message = "Username is required")
    @Size(min = 4, max = 50)
    private String username;

    @NotBlank(message = "Password is required")
    private String password;

    @NotNull(message = "Center ID is required")
    private Long centerId;

    @NotBlank(message = "Registration number is required")
    private String registrationNo;

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    private LocalDate dateOfBirth;

    private String gender;

    @NotBlank(message = "Mobile is required")
    private String mobile;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    private String fatherName;
    private String motherName;
    private String guardianName;
    private String guardianMobile;
    private String aadhaarNo;
    private String qualification;
    private String address;
    private String city;
    private String state;
    private String pincode;
    private LocalDate registrationDate;
    private String status;

    @NotNull(message = "Course ID is required")
    private Long courseId;

    private Long trainerId;

    private LocalDate startDate;
    private LocalDate expectedEndDate;

    private BigDecimal discount;
    private BigDecimal initialPayment;

    private String paymentMode;
    private String transactionReference;
    private LocalDate feeDueDate;
    private String remarks;
}
