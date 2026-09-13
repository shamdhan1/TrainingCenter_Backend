package com.trainingcenter.trainer.service;

import com.trainingcenter.trainer.dto.request.TrainerRegistrationRequest;
import com.trainingcenter.trainer.dto.response.TrainerRegistrationResponse;

import java.util.List;

public interface TrainerRegistrationService {
    List<TrainerRegistrationResponse> getAllTrainer();
    TrainerRegistrationResponse registerTrainer(TrainerRegistrationRequest request);
}
