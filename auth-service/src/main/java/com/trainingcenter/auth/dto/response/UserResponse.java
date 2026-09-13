package com.trainingcenter.auth.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {

    private Long userId;
    private String username;
    private String email;
    private String mobile;
    private String status;
    private Long centerId;
    private Long studentId;
    private Long trainerId;
    private List<String> roles;
}
