package com.trainingcenter.controller;

import com.trainingcenter.dto.request.AdmissionRequest;
import com.trainingcenter.dto.response.AdmissionResponse;
import com.trainingcenter.dto.response.ApiResponse;
import com.trainingcenter.service.AdmissionService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admissions")
public class AdmissionController {

    private final AdmissionService admissionService;

    @PostMapping
    public ResponseEntity<ApiResponse<AdmissionResponse>> createAdmission(
            @Valid @RequestBody AdmissionRequest request) {

        AdmissionResponse response = admissionService.createAdmission(request);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Student admission created successfully",
                        response));
    }

}
