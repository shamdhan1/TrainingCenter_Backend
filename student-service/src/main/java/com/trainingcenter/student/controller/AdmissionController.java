package com.trainingcenter.student.controller;

import com.trainingcenter.student.dto.request.AdmissionRequest;
import com.trainingcenter.student.dto.response.AdmissionResponse;
import com.trainingcenter.student.dto.response.ApiResponse;
import com.trainingcenter.student.service.AdmissionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admissions")
public class AdmissionController {

    private final AdmissionService admissionService;

    @PostMapping
    public ResponseEntity<ApiResponse<AdmissionResponse>> createAdmission(
            @Valid @RequestBody AdmissionRequest request) {
        AdmissionResponse response = admissionService.createAdmission(request);
        return ResponseEntity.ok(ApiResponse.success("Student admission created successfully", response));
    }
}
