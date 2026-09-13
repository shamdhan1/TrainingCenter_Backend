package com.trainingcenter.trainer.service;

import com.trainingcenter.trainer.dto.response.TrainerReportResponse;

import java.time.LocalDate;

public interface TrainerReportService {
    TrainerReportResponse generateReport(Long trainerId, LocalDate fromDate, LocalDate toDate);
}
