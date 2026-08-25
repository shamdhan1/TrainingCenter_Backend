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
public class CenterResponse {
    private Long centerId;
    private String centerCode;
    private String name;
    private String address;
    private String phone;
    private String email;
    private String status;
    private LocalDateTime createdAt;
}
