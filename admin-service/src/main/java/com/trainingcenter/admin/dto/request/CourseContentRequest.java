package com.trainingcenter.admin.dto.request;

import com.trainingcenter.admin.enums.ContentType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CourseContentRequest {

    @NotNull(message = "Module number is required")
    @Min(value = 1, message = "Module number must be at least 1")
    private Integer moduleNumber;

    @NotBlank(message = "Topic title is required")
    @Size(max = 150, message = "Topic title must not exceed 150 characters")
    private String topicTitle;

    private String description;

    @NotNull(message = "Content type is required")
    private ContentType contentType;

    @Size(max = 500, message = "Resource link must not exceed 500 characters")
    private String resourceLink;
}
