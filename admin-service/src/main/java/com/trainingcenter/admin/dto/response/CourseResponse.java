package com.trainingcenter.admin.dto.response;

import com.trainingcenter.admin.enums.CourseStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourseResponse {
    private Long courseId;
    private String courseCode;
    private String courseName;
    private String description;
    private Integer durationWeeks;
    private BigDecimal fees;
    private CourseStatus status;
    private Long centerId;
    private String centerName;
}
