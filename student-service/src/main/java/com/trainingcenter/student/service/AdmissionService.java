package com.trainingcenter.student.service;

import com.trainingcenter.student.dto.request.AdmissionRequest;
import com.trainingcenter.student.dto.response.AdmissionResponse;

public interface AdmissionService {
    AdmissionResponse createAdmission(AdmissionRequest request);
}
