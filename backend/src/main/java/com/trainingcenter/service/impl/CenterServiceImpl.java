package com.trainingcenter.service.impl;

import com.trainingcenter.dto.request.CenterRequest;
import com.trainingcenter.dto.response.CenterResponse;
import com.trainingcenter.entity.Center;
import com.trainingcenter.exception.ConflictException;
import com.trainingcenter.exception.ResourceNotFoundException;
import com.trainingcenter.mapper.CenterMapper;
import com.trainingcenter.repository.CenterRepository;
import com.trainingcenter.service.CenterService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CenterServiceImpl implements CenterService {

    private final CenterRepository centerRepository;
    private final CenterMapper centerMapper;

    // Use constructor injection to satisfy dependencies
    public CenterServiceImpl(CenterRepository centerRepository, CenterMapper centerMapper) {
        this.centerRepository = centerRepository;
        this.centerMapper = centerMapper;
    }

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

        centerMapper.updateEntity(center, centerRequest);
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
