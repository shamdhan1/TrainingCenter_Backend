package com.trainingcenter.trainer.controller;

import com.trainingcenter.trainer.dto.response.ApiResponse;
import com.trainingcenter.trainer.dto.response.TrainerReportResponse;
import com.trainingcenter.trainer.service.TrainerReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/trainer-reports")
public class TrainerReportController {

    private final TrainerReportService trainerReportService;

    @GetMapping("/{trainerId}")
    public ResponseEntity<ApiResponse<TrainerReportResponse>> generateReport(
            @PathVariable Long trainerId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate) {
        TrainerReportResponse trainerReportResponse = trainerReportService.generateReport(trainerId, fromDate, toDate);
        return ResponseEntity.ok(ApiResponse.success("Trainer Report generated successfully", trainerReportResponse));
    }
}
