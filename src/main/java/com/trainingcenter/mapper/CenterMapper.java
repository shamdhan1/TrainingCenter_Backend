package com.trainingcenter.mapper;

import com.trainingcenter.dto.request.CenterRequest;
import com.trainingcenter.dto.response.CenterResponse;
import com.trainingcenter.entity.Center;
import com.trainingcenter.enums.CenterStatus;
import org.springframework.stereotype.Component;

@Component
public class CenterMapper {

    // CenterMapper encapsulates mapping logic between DTOs and entities
    
    public Center toEntity(CenterRequest request) {
        if (request == null) {
            return null;
        }
        return Center.builder()
                .centerCode(request.getCenterCode().toUpperCase())
                .name(request.getName())
                .address(request.getAddress())
                .phone(request.getPhone())
                .email(request.getEmail())
                .status(CenterStatus.valueOf(request.getStatus().toUpperCase()))
                .build();
    }

    public CenterResponse toResponse(Center entity) {
        if (entity == null) {
            return null;
        }
        return CenterResponse.builder()
                .centerId(entity.getCenterId())
                .centerCode(entity.getCenterCode())
                .name(entity.getName())
                .address(entity.getAddress())
                .phone(entity.getPhone())
                .email(entity.getEmail())
                .status(entity.getStatus().name())
                .createdAt(entity.getCreatedAt())
                .build();
    }

    public void updateEntity(Center entity, CenterRequest request) {
        if (request == null || entity == null) {
            return;
        }
        entity.setCenterCode(request.getCenterCode().toUpperCase());
        entity.setName(request.getName());
        entity.setAddress(request.getAddress());
        entity.setPhone(request.getPhone());
        entity.setEmail(request.getEmail());
        entity.setStatus(CenterStatus.valueOf(request.getStatus().toUpperCase()));
    }
}
