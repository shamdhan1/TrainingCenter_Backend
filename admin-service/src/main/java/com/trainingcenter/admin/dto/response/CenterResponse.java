package com.trainingcenter.admin.dto.response;

import com.trainingcenter.admin.enums.CenterStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CenterResponse {
    private Long centerId;
    private String centerCode;
    private String centerName;
    private String location;
    private String contactNumber;
    private CenterStatus status;
}
