package com.trainingcenter.service;

import com.trainingcenter.dto.request.TrainerRegistrationRequest;
import com.trainingcenter.dto.response.TrainerRegistrationResponse;

public interface TrainerRegistrationService {
    TrainerRegistrationResponse registerTrainer(TrainerRegistrationRequest request);
}
