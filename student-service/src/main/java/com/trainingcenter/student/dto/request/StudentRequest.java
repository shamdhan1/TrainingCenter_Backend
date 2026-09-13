package com.trainingcenter.student.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
public class StudentRequest {

    private String username;
    private String password;

    @NotNull(message = "Center ID is required")
    private Long centerId;

    @NotBlank(message = "Registration number is required")
    @Size(max = 30, message = "Registration number must be under 30 characters")
    private String registrationNo;

    @NotBlank(message = "First name is required")
    @Size(max = 50, message = "First name must be under 50 characters")
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(max = 50, message = "Last name must be under 50 characters")
    private String lastName;

    private LocalDate dateOfBirth;

    @Size(max = 15, message = "Gender must be under 15 characters")
    private String gender;

    @NotBlank(message = "Mobile is required")
    @Size(max = 15, message = "Mobile must be under 15 characters")
    private String mobile;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @Size(max = 100, message = "Email must be under 100 characters")
    private String email;

    @Size(max = 255, message = "Address must be under 255 characters")
    private String address;

    @Size(max = 100, message = "Qualification must be under 100 characters")
    private String qualification;

    @Size(max = 100, message = "Father name must be under 100 characters")
    private String fatherName;

    @Size(max = 100, message = "Mother name must be under 100 characters")
    private String motherName;

    @Size(max = 100, message = "Guardian name must be under 100 characters")
    private String guardianName;

    @Size(max = 15, message = "Guardian mobile must be under 15 characters")
    private String guardianMobile;

    @Size(max = 12, message = "Aadhaar number must be under 12 characters")
    private String aadhaarNo;

    @Size(max = 50)
    private String city;

    @Size(max = 50)
    private String state;

    @Size(max = 10)
    private String pincode;

    private LocalDate registrationDate;

    private String status;
}
