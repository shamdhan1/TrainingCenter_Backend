package com.trainingcenter.controller;

import com.trainingcenter.dto.request.TrainerRegistrationRequest;
import com.trainingcenter.dto.response.TrainerRegistrationResponse;
import com.trainingcenter.dto.response.ApiResponse;
import com.trainingcenter.service.TrainerRegistrationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/trainers")
public class TrainerRegistrationController {

    private final TrainerRegistrationService trainerRegistrationService;


    @GetMapping("/all")
    public ResponseEntity<List<TrainerRegistrationResponse>> getRagisterTrainers(){
        List<TrainerRegistrationResponse> allTrainer = trainerRegistrationService.getAllTrainer();
        return ResponseEntity.ok(allTrainer);
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<TrainerRegistrationResponse>> registerTrainer(
            @Valid @RequestBody TrainerRegistrationRequest request) {

        TrainerRegistrationResponse response = trainerRegistrationService.registerTrainer(request);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Trainer registered successfully",
                        response));
    }




}
