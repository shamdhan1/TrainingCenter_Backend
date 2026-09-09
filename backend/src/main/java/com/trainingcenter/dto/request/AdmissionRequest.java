package com.trainingcenter.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class AdmissionRequest {

    // ==============================
    // USER ACCOUNT
    // ==============================

    @NotBlank
    @Size(min = 4, max = 50)
    private String username;

    @NotBlank
    @Size(min = 6, max = 50)
    private String password;

    // ==============================
    // CENTER
    // ==============================

    @NotNull
    private Long centerId;

    // ==============================
    // STUDENT
    // ==============================

    @NotBlank
    @Size(max = 30)
    private String registrationNo;

    @NotBlank
    @Size(max = 50)
    private String firstName;

    @NotBlank
    @Size(max = 50)
    private String lastName;

    private LocalDate dateOfBirth;

    private String gender;

    @NotBlank
    @Size(max = 15)
    private String mobile;

    @NotBlank
    @Email
    private String email;

    private String qualification;

    private String address;

    private String city;

    private String state;

    private String pincode;

    // ==============================
    // FAMILY
    // ==============================

    private String fatherName;

    private String motherName;

    private String guardianName;

    private String guardianMobile;

    private String aadhaarNo;

    // ==============================
    // ADMISSION
    // ==============================

    private LocalDate registrationDate;

    private String status;

    // ==============================
    // COURSE
    // ==============================

    @NotNull
    private Long courseId;

    private Long trainerId;

    private LocalDate startDate;

    private LocalDate expectedEndDate;

    // ==============================
    // FEES
    // ==============================

    private BigDecimal discount = BigDecimal.ZERO;

    private BigDecimal initialPayment = BigDecimal.ZERO;

    private String paymentMode;

    private String transactionReference;

    private LocalDate feeDueDate;

    // ==============================
    // REMARKS
    // ==============================

    private String remarks;

}