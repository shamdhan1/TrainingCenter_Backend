package com.trainingcenter.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrainerRegistrationRequest {

    @NotBlank(message = "Username is required")
    @Size(min = 4, max = 50)
    private String username;

    @NotBlank(message = "Password is required")
    private String password;

    @NotBlank(message = "Name is required")
    @Size(max = 100)
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @Size(max = 100)
    private String email;

    @NotBlank(message = "Mobile number is required")
    @Size(max = 15)
    private String mobile;

    @NotBlank(message = "Employee code is required")
    @Size(max = 30)
    private String employeeCode;

    @Size(max = 100)
    private String specialization;

    @Size(max = 100)
    private String qualification;

    private Integer experienceYears;

    private LocalDate joiningDate;

    @NotNull(message = "Center ID is required")
    private Long centerId;

    private String status; // ACTIVE, PENDING, INACTIVE

    // ==========================================
    // EXTENDED ONBOARDING & PROFILE FIELDS
    // ==========================================

    private String gender;

    private LocalDate dateOfBirth;

    @Size(max = 12)
    private String aadhaarNo;

    @Size(max = 10)
    private String panNo;

    @Size(max = 15)
    private String alternativeMobile;

    @Size(max = 255)
    private String address;

    @Size(max = 50)
    private String city;

    @Size(max = 50)
    private String state;

    @Size(max = 10)
    private String pincode;

    @Size(max = 50)
    private String designation;

    private String contractType; // e.g. FULL_TIME, PART_TIME, CONTRACT, GUEST

    private BigDecimal salary;

    // Payroll Bank details
    @Size(max = 100)
    private String bankName;

    @Size(max = 30)
    private String bankAccountNumber;

    @Size(max = 20)
    private String ifscCode;

    // Bio profile
    private String bio;
}
