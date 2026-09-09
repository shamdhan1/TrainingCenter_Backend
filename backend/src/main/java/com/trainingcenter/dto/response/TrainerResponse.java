package com.trainingcenter.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TrainerResponse {
    private Long trainerId;
    private Long userId;
    private String username;
    private Long centerId;
    private String centerName;
    private String employeeCode;
    private String name;
    private String mobile;
    private String email;
    private String specialization;
    private String qualification;
    private Integer experienceYears;
    private LocalDate joiningDate;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

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
