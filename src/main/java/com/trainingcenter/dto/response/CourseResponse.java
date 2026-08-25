package com.trainingcenter.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseResponse {
    private Long courseId;
    private Long centerId;
    private String centerName;
    private String courseCode;
    private String courseName;
    private String description;
    private String duration;
    private BigDecimal totalFee;
    private String status;
    private LocalDateTime createdAt;
}
