package com.trainingcenter.admin.dto.response;

import com.trainingcenter.admin.enums.ContentType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourseContentResponse {
    private Long contentId;
    private Long courseId;
    private Integer moduleNumber;
    private String topicTitle;
    private String description;
    private ContentType contentType;
    private String resourceLink;
}
