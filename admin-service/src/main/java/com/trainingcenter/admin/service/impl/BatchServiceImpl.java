package com.trainingcenter.admin.service.impl;

import com.trainingcenter.admin.dto.request.BatchRequest;
import com.trainingcenter.admin.dto.response.BatchResponse;
import com.trainingcenter.admin.entity.Batch;
import com.trainingcenter.admin.entity.Center;
import com.trainingcenter.admin.entity.Course;
import com.trainingcenter.admin.enums.CourseStatus;
import com.trainingcenter.admin.exception.ConflictException;
import com.trainingcenter.admin.exception.ResourceNotFoundException;
import com.trainingcenter.admin.repository.BatchRepository;
import com.trainingcenter.admin.repository.CenterRepository;
import com.trainingcenter.admin.repository.CourseRepository;
import com.trainingcenter.admin.service.BatchService;
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
public class BatchServiceImpl implements BatchService {

    private final BatchRepository batchRepository;
    private final CourseRepository courseRepository;
    private final CenterRepository centerRepository;

    @Override
    @Transactional(readOnly = true)
    public List<BatchResponse> getBatchesByCenter(Long centerId) {
        List<Batch> batches;
        if (centerId == null) {
            batches = batchRepository.findAll();
        } else {
            batches = batchRepository.findByCenterCenterId(centerId);
        }
        return batches.stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BatchResponse> getBatchesByCenterPaginated(Long centerId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Batch> batches;
        if (centerId == null) {
            batches = batchRepository.findAll(pageable);
        } else {
            batches = batchRepository.findByCenterCenterId(centerId, pageable);
        }
        return batches.map(this::mapToResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public BatchResponse getBatchById(Long id) {
        Batch batch = batchRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Batch not found with ID: " + id));
        return mapToResponse(batch);
    }

    @Override
    @Transactional
    public BatchResponse createBatch(BatchRequest request) {
        if (batchRepository.existsByBatchCode(request.getBatchCode())) {
            throw new ConflictException("Batch code already exists: " + request.getBatchCode());
        }

        Course course = courseRepository.findById(request.getCourseId())
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with ID: " + request.getCourseId()));

        Center center = centerRepository.findById(request.getCenterId())
                .orElseThrow(() -> new ResourceNotFoundException("Center not found with ID: " + request.getCenterId()));

        Batch batch = Batch.builder()
                .batchCode(request.getBatchCode().toUpperCase())
                .batchName(request.getBatchName())
                .course(course)
                .center(center)
                .trainerId(request.getTrainerId())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .timings(request.getTimings())
                .maxStudents(request.getMaxStudents())
                .status(request.getStatus() != null ? request.getStatus() : CourseStatus.ACTIVE)
                .build();

        return mapToResponse(batchRepository.save(batch));
    }

    @Override
    @Transactional
    public BatchResponse updateBatch(Long id, BatchRequest request) {
        Batch batch = batchRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Batch not found with ID: " + id));

        if (!batch.getBatchCode().equalsIgnoreCase(request.getBatchCode()) &&
                batchRepository.existsByBatchCode(request.getBatchCode())) {
            throw new ConflictException("Batch code already exists: " + request.getBatchCode());
        }

        Course course = courseRepository.findById(request.getCourseId())
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with ID: " + request.getCourseId()));

        Center center = centerRepository.findById(request.getCenterId())
                .orElseThrow(() -> new ResourceNotFoundException("Center not found with ID: " + request.getCenterId()));

        batch.setBatchCode(request.getBatchCode().toUpperCase());
        batch.setBatchName(request.getBatchName());
        batch.setCourse(course);
        batch.setCenter(center);
        batch.setTrainerId(request.getTrainerId());
        batch.setStartDate(request.getStartDate());
        batch.setEndDate(request.getEndDate());
        batch.setTimings(request.getTimings());
        batch.setMaxStudents(request.getMaxStudents());
        if (request.getStatus() != null) {
            batch.setStatus(request.getStatus());
        }

        return mapToResponse(batchRepository.save(batch));
    }

    @Override
    @Transactional
    public void deleteBatch(Long id) {
        if (!batchRepository.existsById(id)) {
            throw new ResourceNotFoundException("Batch not found with ID: " + id);
        }
        batchRepository.deleteById(id);
    }

    private BatchResponse mapToResponse(Batch batch) {
        return BatchResponse.builder()
                .batchId(batch.getBatchId())
                .batchCode(batch.getBatchCode())
                .batchName(batch.getBatchName())
                .courseId(batch.getCourse() != null ? batch.getCourse().getCourseId() : null)
                .courseName(batch.getCourse() != null ? batch.getCourse().getCourseName() : null)
                .centerId(batch.getCenter() != null ? batch.getCenter().getCenterId() : null)
                .centerName(batch.getCenter() != null ? batch.getCenter().getCenterName() : null)
                .trainerId(batch.getTrainerId())
                .startDate(batch.getStartDate())
                .endDate(batch.getEndDate())
                .timings(batch.getTimings())
                .maxStudents(batch.getMaxStudents())
                .status(batch.getStatus())
                .build();
    }
}
