package com.trainingcenter.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentResponse {
    private Long studentId;
    private Long userId;
    private String username;
    private Long centerId;
    private String centerName;
    private String registrationNo;
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private String gender;
    private String mobile;
    private String email;
    private String address;
    private String qualification;
    private String fatherName;
    private String motherName;
    private String guardianName;
    private String guardianMobile;
    private String aadhaarNo;
    private String city;
    private String state;
    private String pincode;
    private LocalDate registrationDate;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
