package com.trainingcenter.trainer.dto.request;

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

    private String username;
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

    private String status;

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
