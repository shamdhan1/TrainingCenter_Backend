package com.trainingcenter.admin.controller;

import com.trainingcenter.admin.dto.request.CenterRequest;
import com.trainingcenter.admin.dto.response.ApiResponse;
import com.trainingcenter.admin.dto.response.CenterResponse;
import com.trainingcenter.admin.service.CenterService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/centers")
@RequiredArgsConstructor
public class CenterController {

    private final CenterService centerService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<CenterResponse>>> getAllCenters() {
        List<CenterResponse> centers = centerService.getAllCenters();
        return ResponseEntity.ok(ApiResponse.success("Centers fetched successfully", centers));
    }

    @GetMapping("/paginated")
    public ResponseEntity<ApiResponse<Page<CenterResponse>>> getAllCentersPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<CenterResponse> centers = centerService.getAllCentersPaginated(page, size);
        return ResponseEntity.ok(ApiResponse.success("Centers fetched successfully", centers));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CenterResponse>> getCenterById(@PathVariable Long id) {
        CenterResponse center = centerService.getCenterById(id);
        return ResponseEntity.ok(ApiResponse.success("Center fetched successfully", center));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<CenterResponse>> createCenter(@Valid @RequestBody CenterRequest centerRequest) {
        CenterResponse center = centerService.createCenter(centerRequest);
        return ResponseEntity.ok(ApiResponse.success("Center created successfully", center));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CenterResponse>> updateCenter(
            @PathVariable Long id,
            @Valid @RequestBody CenterRequest centerRequest) {
        CenterResponse center = centerService.updateCenter(id, centerRequest);
        return ResponseEntity.ok(ApiResponse.success("Center updated successfully", center));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteCenter(@PathVariable Long id) {
        centerService.deleteCenter(id);
        return ResponseEntity.ok(ApiResponse.success("Center deleted successfully", "Deleted center with ID: " + id));
    }
}
