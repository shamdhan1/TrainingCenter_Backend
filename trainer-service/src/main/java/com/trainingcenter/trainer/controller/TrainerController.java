package com.trainingcenter.trainer.controller;

import com.trainingcenter.trainer.dto.request.TrainerRequest;
import com.trainingcenter.trainer.dto.response.ApiResponse;
import com.trainingcenter.trainer.dto.response.TrainerResponse;
import com.trainingcenter.trainer.service.TrainerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/trainers")
@RequiredArgsConstructor
public class TrainerController {

    private final TrainerService trainerService;

    @GetMapping("/count")
    public ResponseEntity<Long> getTrainerCount() {
        return ResponseEntity.ok(trainerService.countTrainers());
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<TrainerResponse>>> searchTrainers(
            @RequestParam(required = false) String query,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Long centerId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<TrainerResponse> trainers = trainerService.searchTrainers(query, status, centerId, page, size);
        return ResponseEntity.ok(ApiResponse.success("Trainers fetched successfully", trainers));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TrainerResponse>> getTrainerById(@PathVariable Long id) {
        TrainerResponse trainer = trainerService.getTrainerById(id);
        return ResponseEntity.ok(ApiResponse.success("Trainer profile fetched successfully", trainer));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<TrainerResponse>> createTrainer(@Valid @RequestBody TrainerRequest request) {
        TrainerResponse trainer = trainerService.createTrainer(request);
        return ResponseEntity.ok(ApiResponse.success("Trainer registered successfully", trainer));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<TrainerResponse>> updateTrainer(
            @PathVariable Long id,
            @Valid @RequestBody TrainerRequest request) {
        TrainerResponse trainer = trainerService.updateTrainer(id, request);
        return ResponseEntity.ok(ApiResponse.success("Trainer updated successfully", trainer));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteTrainer(@PathVariable Long id) {
        trainerService.deleteTrainer(id);
        return ResponseEntity.ok(ApiResponse.success("Trainer deleted successfully", "Deleted trainer with ID: " + id));
    }
}
