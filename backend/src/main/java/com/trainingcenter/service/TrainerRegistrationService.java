package com.trainingcenter.service;

import com.trainingcenter.dto.request.TrainerRegistrationRequest;
import com.trainingcenter.dto.response.TrainerRegistrationResponse;

import java.util.List;

public interface TrainerRegistrationService {
    TrainerRegistrationResponse registerTrainer(TrainerRegistrationRequest request);
    List<TrainerRegistrationResponse> getAllTrainer();
}

