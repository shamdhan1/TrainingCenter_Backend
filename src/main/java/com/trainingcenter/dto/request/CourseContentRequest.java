package com.trainingcenter.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CourseContentRequest {

    @NotNull(message = "Module number is required")
    @Min(value = 1, message = "Module number must be at least 1")
    private Integer moduleNo;

    @NotBlank(message = "Title is required")
    @Size(max = 150, message = "Title must be under 150 characters")
    private String title;

    private String description;

    @NotBlank(message = "Content type is required")
    private String contentType; // VIDEO, DOCUMENT, PDF, LINK, TEXT, ASSIGNMENT

    @Size(max = 255, message = "Resource URL must be under 255 characters")
    private String resourceUrl;

    @NotNull(message = "Sequence number is required")
    @Min(value = 1, message = "Sequence number must be at least 1")
    private Integer sequenceNo;

    @Min(value = 0, message = "Duration must be positive")
    private Integer durationMinutes;
}
