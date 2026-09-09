package com.trainingcenter.service.impl;

import com.trainingcenter.dto.request.BatchRequest;
import com.trainingcenter.dto.response.BatchResponse;
import com.trainingcenter.entity.Batch;
import com.trainingcenter.entity.Center;
import com.trainingcenter.entity.Course;
import com.trainingcenter.entity.Trainer;
import com.trainingcenter.enums.CourseStatus;
import com.trainingcenter.repository.BatchRepository;
import com.trainingcenter.repository.CenterRepository;
import com.trainingcenter.repository.CourseRepository;
import com.trainingcenter.repository.TrainerRepository;
import com.trainingcenter.service.BatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BatchServiceImpl implements BatchService {

    @Autowired
    private BatchRepository batchRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private TrainerRepository trainerRepository;

    @Autowired
    private CenterRepository centerRepository;

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
                .orElseThrow(() -> new RuntimeException("Batch not found with ID: " + id));
        return mapToResponse(batch);
    }

    @Override
    @Transactional
    public BatchResponse createBatch(BatchRequest request) {
        if (batchRepository.existsByBatchCode(request.getBatchCode())) {
            throw new RuntimeException("Batch code already exists: " + request.getBatchCode());
        }

        Course course = courseRepository.findById(request.getCourseId())
                .orElseThrow(() -> new RuntimeException("Course not found with ID: " + request.getCourseId()));

        Center center = centerRepository.findById(request.getCenterId())
                .orElseThrow(() -> new RuntimeException("Center not found with ID: " + request.getCenterId()));

        Trainer trainer = null;
        if (request.getTrainerId() != null) {
            trainer = trainerRepository.findById(request.getTrainerId())
                    .orElseThrow(() -> new RuntimeException("Trainer not found with ID: " + request.getTrainerId()));
        }

        Batch batch = Batch.builder()
                .course(course)
                .center(center)
                .trainer(trainer)
                .batchCode(request.getBatchCode().toUpperCase().trim())
                .batchName(request.getBatchName())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .maxStudents(request.getMaxStudents())
                .status(CourseStatus.valueOf(request.getStatus().toUpperCase()))
                .build();

        return mapToResponse(batchRepository.save(batch));
    }

    @Override
    @Transactional
    public BatchResponse updateBatch(Long id, BatchRequest request) {
        Batch batch = batchRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Batch not found with ID: " + id));

        if (!batch.getBatchCode().equalsIgnoreCase(request.getBatchCode()) &&
                batchRepository.existsByBatchCode(request.getBatchCode())) {
            throw new RuntimeException("Batch code already exists: " + request.getBatchCode());
        }

        Course course = courseRepository.findById(request.getCourseId())
                .orElseThrow(() -> new RuntimeException("Course not found with ID: " + request.getCourseId()));

        Center center = centerRepository.findById(request.getCenterId())
                .orElseThrow(() -> new RuntimeException("Center not found with ID: " + request.getCenterId()));

        Trainer trainer = null;
        if (request.getTrainerId() != null) {
            trainer = trainerRepository.findById(request.getTrainerId())
                    .orElseThrow(() -> new RuntimeException("Trainer not found with ID: " + request.getTrainerId()));
        }

        batch.setCourse(course);
        batch.setCenter(center);
        batch.setTrainer(trainer);
        batch.setBatchCode(request.getBatchCode().toUpperCase().trim());
        batch.setBatchName(request.getBatchName());
        batch.setStartDate(request.getStartDate());
        batch.setEndDate(request.getEndDate());
        batch.setMaxStudents(request.getMaxStudents());
        batch.setStatus(CourseStatus.valueOf(request.getStatus().toUpperCase()));

        return mapToResponse(batchRepository.save(batch));
    }

    @Override
    @Transactional
    public void deleteBatch(Long id) {
        if (!batchRepository.existsById(id)) {
            throw new RuntimeException("Batch not found with ID: " + id);
        }
        batchRepository.deleteById(id);
    }

    private BatchResponse mapToResponse(Batch batch) {
        return BatchResponse.builder()
                .batchId(batch.getBatchId())
                .courseId(batch.getCourse().getCourseId())
                .courseName(batch.getCourse().getCourseName())
                .trainerId(batch.getTrainer() != null ? batch.getTrainer().getTrainerId() : null)
                .trainerName(batch.getTrainer() != null ? batch.getTrainer().getName() : "Unassigned")
                .centerId(batch.getCenter().getCenterId())
                .centerName(batch.getCenter().getName())
                .batchCode(batch.getBatchCode())
                .batchName(batch.getBatchName())
                .startDate(batch.getStartDate())
                .endDate(batch.getEndDate())
                .maxStudents(batch.getMaxStudents())
                .status(batch.getStatus().name())
                .createdAt(batch.getCreatedAt())
                .build();
    }
}
