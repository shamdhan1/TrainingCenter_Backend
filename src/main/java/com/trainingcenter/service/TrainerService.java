package com.trainingcenter.service;

import com.trainingcenter.dto.request.TrainerRequest;
import com.trainingcenter.dto.response.TrainerResponse;
import org.springframework.data.domain.Page;

public interface TrainerService {
    Page<TrainerResponse> searchTrainers(String query, String status, Long centerId, int page, int size);
    TrainerResponse getTrainerById(Long id);
    TrainerResponse getTrainerByUserId(Long userId);
    TrainerResponse createTrainer(TrainerRequest request);
    TrainerResponse updateTrainer(Long id, TrainerRequest request);
    void deleteTrainer(Long id);
}
