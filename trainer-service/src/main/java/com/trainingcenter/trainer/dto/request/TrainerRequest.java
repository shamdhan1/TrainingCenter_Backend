package com.trainingcenter.trainer.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class TrainerRequest {

    private String username;
    private String password;

    @NotNull(message = "Center ID is required")
    private Long centerId;

    @NotBlank(message = "Employee code is required")
    @Size(max = 30, message = "Employee code must be under 30 characters")
    private String employeeCode;

    @NotBlank(message = "Name is required")
    @Size(max = 100, message = "Name must be under 100 characters")
    private String name;

    @NotBlank(message = "Mobile is required")
    @Size(max = 15, message = "Mobile must be under 15 characters")
    private String mobile;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @Size(max = 100, message = "Email must be under 100 characters")
    private String email;

    @Size(max = 100, message = "Specialization must be under 100 characters")
    private String specialization;

    @Size(max = 100, message = "Qualification must be under 100 characters")
    private String qualification;

    private Integer experienceYears;
    private LocalDate joiningDate;
    private String status; // ACTIVE, INACTIVE, PENDING

    private String gender;
    private LocalDate dateOfBirth;
    private String aadhaarNo;
    private String panNo;
    private String alternativeMobile;
    private String address;
    private String city;
    private String state;
    private String pincode;
    private String designation;
    private String contractType;
    private BigDecimal salary;
    private String bankName;
    private String bankAccountNumber;
    private String ifscCode;
    private String bio;
}
