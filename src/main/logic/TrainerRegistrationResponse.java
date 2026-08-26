package com.trainingcenter.dto.response;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrainerRegistrationResponse {
    private Long trainerId;
    private Long userId;
    private String employeeCode;
    private String name;
    private String email;
    private String centerName;
    private String status;
    private String message;

    // Extended profile data
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
