package com.trainingcenter.service;

import com.trainingcenter.dto.request.AdmissionRequest;
import com.trainingcenter.dto.response.AdmissionResponse;

public interface AdmissionService {

    AdmissionResponse createAdmission(AdmissionRequest request);
}
