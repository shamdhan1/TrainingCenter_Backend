package com.trainingcenter.admin.dto.request;

import com.trainingcenter.admin.enums.CenterStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CenterRequest {

    @NotBlank(message = "Center code is required")
    @Size(max = 20, message = "Center code must not exceed 20 characters")
    private String centerCode;

    @NotBlank(message = "Center name is required")
    @Size(max = 100, message = "Center name must not exceed 100 characters")
    private String centerName;

    @NotBlank(message = "Location is required")
    @Size(max = 200, message = "Location must not exceed 200 characters")
    private String location;

    @Pattern(regexp = "^[0-9]{10}$", message = "Contact number must be exactly 10 digits")
    private String contactNumber;

    private CenterStatus status;
}
