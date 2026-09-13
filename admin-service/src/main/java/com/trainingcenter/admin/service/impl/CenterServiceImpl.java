package com.trainingcenter.admin.service.impl;

import com.trainingcenter.admin.dto.request.CenterRequest;
import com.trainingcenter.admin.dto.response.CenterResponse;
import com.trainingcenter.admin.entity.Center;
import com.trainingcenter.admin.exception.ConflictException;
import com.trainingcenter.admin.exception.ResourceNotFoundException;
import com.trainingcenter.admin.mapper.CenterMapper;
import com.trainingcenter.admin.repository.CenterRepository;
import com.trainingcenter.admin.service.CenterService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CenterServiceImpl implements CenterService {

    private final CenterRepository centerRepository;
    private final CenterMapper centerMapper;

    @Override
    @Transactional(readOnly = true)
    public List<CenterResponse> getAllCenters() {
        return centerRepository.findAll().stream()
                .map(centerMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CenterResponse> getAllCentersPaginated(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return centerRepository.findAll(pageable).map(centerMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public CenterResponse getCenterById(Long centerId) {
        Center center = centerRepository.findById(centerId)
                .orElseThrow(() -> new ResourceNotFoundException("Center not found with ID: " + centerId));
        return centerMapper.toResponse(center);
    }

    @Override
    @Transactional
    public CenterResponse createCenter(CenterRequest centerRequest) {
        if (centerRepository.existsByCenterCode(centerRequest.getCenterCode())) {
            throw new ConflictException("Center code already exists: " + centerRequest.getCenterCode());
        }

        Center center = centerMapper.toEntity(centerRequest);
        Center savedCenter = centerRepository.save(center);
        return centerMapper.toResponse(savedCenter);
    }

    @Override
    @Transactional
    public CenterResponse updateCenter(Long centerId, CenterRequest centerRequest) {
        Center center = centerRepository.findById(centerId)
                .orElseThrow(() -> new ResourceNotFoundException("Center not found with ID: " + centerId));

        if (!center.getCenterCode().equalsIgnoreCase(centerRequest.getCenterCode()) &&
                centerRepository.existsByCenterCode(centerRequest.getCenterCode())) {
            throw new ConflictException("Center code already exists: " + centerRequest.getCenterCode());
        }

        centerMapper.updateEntityFromRequest(center, centerRequest);
        Center updatedCenter = centerRepository.save(center);
        return centerMapper.toResponse(updatedCenter);
    }

    @Override
    @Transactional
    public void deleteCenter(Long centerId) {
        if (!centerRepository.existsById(centerId)) {
            throw new ResourceNotFoundException("Center not found with ID: " + centerId);
        }
        centerRepository.deleteById(centerId);
    }
}
