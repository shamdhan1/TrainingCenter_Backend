package com.trainingcenter.admin.mapper;

import com.trainingcenter.admin.dto.request.CenterRequest;
import com.trainingcenter.admin.dto.response.CenterResponse;
import com.trainingcenter.admin.entity.Center;
import com.trainingcenter.admin.enums.CenterStatus;
import org.springframework.stereotype.Component;

@Component
public class CenterMapper {

    public Center toEntity(CenterRequest request) {
        if (request == null) {
            return null;
        }

        return Center.builder()
                .centerCode(request.getCenterCode())
                .centerName(request.getCenterName())
                .location(request.getLocation())
                .contactNumber(request.getContactNumber())
                .status(request.getStatus() != null ? request.getStatus() : CenterStatus.ACTIVE)
                .build();
    }

    public CenterResponse toResponse(Center center) {
        if (center == null) {
            return null;
        }

        return CenterResponse.builder()
                .centerId(center.getCenterId())
                .centerCode(center.getCenterCode())
                .centerName(center.getCenterName())
                .location(center.getLocation())
                .contactNumber(center.getContactNumber())
                .status(center.getStatus())
                .build();
    }

    public void updateEntityFromRequest(Center center, CenterRequest request) {
        if (center == null || request == null) {
            return;
        }

        if (request.getCenterCode() != null) {
            center.setCenterCode(request.getCenterCode());
        }
        if (request.getCenterName() != null) {
            center.setCenterName(request.getCenterName());
        }
        if (request.getLocation() != null) {
            center.setLocation(request.getLocation());
        }
        if (request.getContactNumber() != null) {
            center.setContactNumber(request.getContactNumber());
        }
        if (request.getStatus() != null) {
            center.setStatus(request.getStatus());
        }
    }
}
