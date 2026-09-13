package com.trainingcenter.admin.controller;

import com.trainingcenter.admin.dto.request.BatchRequest;
import com.trainingcenter.admin.dto.response.ApiResponse;
import com.trainingcenter.admin.dto.response.BatchResponse;
import com.trainingcenter.admin.service.BatchService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/batches")
@RequiredArgsConstructor
public class BatchController {

    private final BatchService batchService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<BatchResponse>>> getBatches(
            @RequestParam(required = false) Long centerId) {
        List<BatchResponse> batches = batchService.getBatchesByCenter(centerId);
        return ResponseEntity.ok(ApiResponse.success("Batches fetched successfully", batches));
    }

    @GetMapping("/paginated")
    public ResponseEntity<ApiResponse<Page<BatchResponse>>> getBatchesPaginated(
            @RequestParam(required = false) Long centerId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<BatchResponse> batches = batchService.getBatchesByCenterPaginated(centerId, page, size);
        return ResponseEntity.ok(ApiResponse.success("Batches fetched successfully", batches));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<BatchResponse>> getBatchById(@PathVariable Long id) {
        BatchResponse batch = batchService.getBatchById(id);
        return ResponseEntity.ok(ApiResponse.success("Batch fetched successfully", batch));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<BatchResponse>> createBatch(@Valid @RequestBody BatchRequest request) {
        BatchResponse batch = batchService.createBatch(request);
        return ResponseEntity.ok(ApiResponse.success("Batch created successfully", batch));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<BatchResponse>> updateBatch(
            @PathVariable Long id,
            @Valid @RequestBody BatchRequest request) {
        BatchResponse batch = batchService.updateBatch(id, request);
        return ResponseEntity.ok(ApiResponse.success("Batch updated successfully", batch));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteBatch(@PathVariable Long id) {
        batchService.deleteBatch(id);
        return ResponseEntity.ok(ApiResponse.success("Batch deleted successfully", "Deleted batch with ID: " + id));
    }
}
