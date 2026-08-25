package com.trainingcenter.controller;

import com.trainingcenter.dto.request.CenterRequest;
import com.trainingcenter.dto.response.ApiResponse;
import com.trainingcenter.dto.response.CenterResponse;
import com.trainingcenter.service.CenterService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/centers")
public class CenterController {

    private final CenterService centerService;

    // Use constructor injection rather than field-based @Autowired
    public CenterController(CenterService centerService) {
        this.centerService = centerService;
    }

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
//    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<CenterResponse>> createCenter(@Valid @RequestBody CenterRequest centerRequest) {
        CenterResponse center = centerService.createCenter(centerRequest);
        return ResponseEntity.ok(ApiResponse.success("Center created successfully", center));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<CenterResponse>> updateCenter(
            @PathVariable Long id,
            @Valid @RequestBody CenterRequest centerRequest) {
        CenterResponse center = centerService.updateCenter(id, centerRequest);
        return ResponseEntity.ok(ApiResponse.success("Center updated successfully", center));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<String>> deleteCenter(@PathVariable Long id) {
        centerService.deleteCenter(id);
        return ResponseEntity.ok(ApiResponse.success("Center deleted successfully", "Deleted center with ID: " + id));
    }
}
