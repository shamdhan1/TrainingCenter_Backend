package com.trainingcenter.controller;


import com.trainingcenter.dto.response.ApiResponse;
import com.trainingcenter.dto.response.TrainerReportResponse;
import com.trainingcenter.service.TrainerReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/trainer-reports")
public class TrainerReportController {

    private final TrainerReportService trainerReportService;

    @org.springframework.web.bind.annotation.GetMapping("/{trainerId}")
    public ResponseEntity<ApiResponse<TrainerReportResponse>> generateReport(
            @PathVariable Long trainerId,
            @RequestParam LocalDate fromDate,
            @RequestParam LocalDate toDate
    ){
        TrainerReportResponse trainerReportResponse = trainerReportService.generateReport(trainerId, fromDate, toDate);

        return ResponseEntity.ok(ApiResponse
                .success("Trainer Report generate Scuccessfully",
                        trainerReportResponse));
    }

}
