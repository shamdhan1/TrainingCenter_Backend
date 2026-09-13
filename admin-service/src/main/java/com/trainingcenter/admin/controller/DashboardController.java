package com.trainingcenter.admin.controller;

import com.trainingcenter.admin.client.StudentClient;
import com.trainingcenter.admin.client.TrainerClient;
import com.trainingcenter.admin.dto.response.ApiResponse;
import com.trainingcenter.admin.repository.CenterRepository;
import com.trainingcenter.admin.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/dashboard")
@RequiredArgsConstructor
@Slf4j
public class DashboardController {

    private final CenterRepository centerRepository;
    private final CourseRepository courseRepository;
    private final TrainerClient trainerClient;
    private final StudentClient studentClient;
    private final RestTemplate restTemplate;

    @GetMapping("/stats")
    public ResponseEntity<ApiResponse<Map<String, Long>>> getStats() {
        Map<String, Long> stats = new HashMap<>();
        stats.put("centersCount", centerRepository.count());
        stats.put("coursesCount", courseRepository.count());

        // 1. Inter-service call via Declarative OpenFeign Client
        long trainersCount = 0L;
        try {
            Long count = trainerClient.getTrainerCount();
            trainersCount = (count != null) ? count : 0L;
        } catch (Exception e) {
            log.warn("Failed to fetch trainer count via OpenFeign: {}. Attempting direct REST fallback...", e.getMessage());
            // 2. Educational demonstration of direct REST via LoadBalanced RestTemplate
            try {
                Long restCount = restTemplate.getForObject("http://trainer-service/api/v1/trainers/count", Long.class);
                trainersCount = (restCount != null) ? restCount : 0L;
            } catch (Exception restEx) {
                log.error("RestTemplate fallback also failed: {}", restEx.getMessage());
            }
        }
        stats.put("trainersCount", trainersCount);

        // Inter-service call for student count via OpenFeign
        long studentsCount = 0L;
        try {
            Long count = studentClient.getStudentCount();
            studentsCount = (count != null) ? count : 0L;
        } catch (Exception e) {
            log.warn("Failed to fetch student count via OpenFeign: {}", e.getMessage());
        }
        stats.put("studentsCount", studentsCount);

        return ResponseEntity.ok(ApiResponse.success("Stats fetched successfully", stats));
    }
}
