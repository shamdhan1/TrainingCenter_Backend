package com.trainingcenter.trainer.service;

import com.trainingcenter.trainer.dto.request.TrainerRequest;
import com.trainingcenter.trainer.dto.response.TrainerResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface TrainerService {
    List<TrainerResponse> getAllTrainers();
    Page<TrainerResponse> searchTrainers(String query, String status, Long centerId, int page, int size);
    TrainerResponse getTrainerById(Long id);
    TrainerResponse createTrainer(TrainerRequest request);
    TrainerResponse updateTrainer(Long id, TrainerRequest request);
    void deleteTrainer(Long id);
    long countTrainers();
}
