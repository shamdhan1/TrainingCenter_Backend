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
}
