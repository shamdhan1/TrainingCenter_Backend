package com.trainingcenter.controller;

import com.trainingcenter.dto.response.ApiResponse;
import com.trainingcenter.repository.CenterRepository;
import com.trainingcenter.repository.CourseRepository;
import com.trainingcenter.repository.StudentRepository;
import com.trainingcenter.repository.TrainerRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/dashboard")
public class DashboardController {

    private final CenterRepository centerRepository;
    private final CourseRepository courseRepository;
    private final TrainerRepository trainerRepository;
    private final StudentRepository studentRepository;

    public DashboardController(CenterRepository centerRepository,
                               CourseRepository courseRepository,
                               TrainerRepository trainerRepository,
                               StudentRepository studentRepository) {
        this.centerRepository = centerRepository;
        this.courseRepository = courseRepository;
        this.trainerRepository = trainerRepository;
        this.studentRepository = studentRepository;
    }

    @GetMapping("/stats")
    public ResponseEntity<ApiResponse<Map<String, Long>>> getStats() {
        Map<String, Long> stats = new HashMap<>();
        stats.put("centersCount", centerRepository.count());
        stats.put("coursesCount", courseRepository.count());
        stats.put("trainersCount", trainerRepository.count());
        stats.put("studentsCount", studentRepository.count());

        return ResponseEntity.ok(ApiResponse.success("Stats fetched successfully", stats));
    }
}
