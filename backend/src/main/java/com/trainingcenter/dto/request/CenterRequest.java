package com.trainingcenter.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CenterRequest {

    @NotBlank(message = "Center code is required")
    @Size(max = 20, message = "Center code must be under 20 characters")
    private String centerCode;

    @NotBlank(message = "Center name is required")
    @Size(max = 100, message = "Center name must be under 100 characters")
    private String name;

    @Size(max = 255, message = "Address must be under 255 characters")
    private String address;

    @Size(max = 20, message = "Phone must be under 20 characters")
    private String phone;

    @Size(max = 100, message = "Email must be under 100 characters")
    private String email;

    @NotBlank(message = "Status is required")
    private String status; // ACTIVE, INACTIVE
}
