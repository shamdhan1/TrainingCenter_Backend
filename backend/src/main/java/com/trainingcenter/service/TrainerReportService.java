package com.trainingcenter.service;

import com.trainingcenter.dto.response.TrainerReportResponse;

import java.time.LocalDate;

public interface TrainerReportService {


    TrainerReportResponse generateReport(
            Long trainerId,
            LocalDate fromDate,
            LocalDate toDate
    );

}
