package com.trainingcenter.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseContentResponse {
    private Long contentId;
    private Long courseId;
    private String courseName;
    private Integer moduleNo;
    private String title;
    private String description;
    private String contentType;
    private String resourceUrl;
    private Integer sequenceNo;
    private Integer durationMinutes;
    private LocalDateTime createdAt;
}
