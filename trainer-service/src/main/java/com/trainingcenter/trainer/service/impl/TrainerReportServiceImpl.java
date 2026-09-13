package com.trainingcenter.trainer.service.impl;

import com.trainingcenter.trainer.dto.response.TrainerReportResponse;
import com.trainingcenter.trainer.entity.Trainer;
import com.trainingcenter.trainer.exception.ResourceNotFoundException;
import com.trainingcenter.trainer.repository.TrainerRepository;
import com.trainingcenter.trainer.service.TrainerReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class TrainerReportServiceImpl implements TrainerReportService {

    private final TrainerRepository trainerRepository;

    @Override
    @Transactional(readOnly = true)
    public TrainerReportResponse generateReport(Long trainerId, LocalDate fromDate, LocalDate toDate) {
        Trainer trainer = trainerRepository.findById(trainerId)
                .orElseThrow(() -> new ResourceNotFoundException("Trainer not found with ID: " + trainerId));

        return TrainerReportResponse.builder()
                .trainerId(trainer.getTrainerId())
                .trainerName(trainer.getName())
                .employeeCode(trainer.getEmployeeCode())
                .specialization(trainer.getSpecialization())
                .fromDate(fromDate)
                .toDate(toDate)
                .totalStudents(0)
                .activeStudents(0)
                .passoutStudents(0)
                .cancelledStudents(0)
                .assessmentsConducted(0)
                .coursesTaught(0)
                .attendancePercentage(0.0)
                .averageMarks(0.0)
                .passRate(0.0)
                .averageProgress(0.0)
                .performanceScore(0.0)
                .students(new ArrayList<>())
                .build();
    }
}
